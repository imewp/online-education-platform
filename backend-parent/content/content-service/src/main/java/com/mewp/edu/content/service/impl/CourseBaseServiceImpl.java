package com.mewp.edu.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mewp.edu.common.exception.CustomException;
import com.mewp.edu.common.model.PageResult;
import com.mewp.edu.common.param.PageParams;
import com.mewp.edu.content.handler.MapResultHandler;
import com.mewp.edu.content.mapper.CourseBaseMapper;
import com.mewp.edu.content.mapper.CourseCategoryMapper;
import com.mewp.edu.content.mapper.CourseMarketMapper;
import com.mewp.edu.content.model.converter.PoDtoConvertMapper;
import com.mewp.edu.content.model.dto.AddOrUpdateCourseDTO;
import com.mewp.edu.content.model.dto.CourseBaseInfoDTO;
import com.mewp.edu.content.model.dto.QueryCourseParamsDTO;
import com.mewp.edu.content.model.po.CourseBase;
import com.mewp.edu.content.model.po.CourseMarket;
import com.mewp.edu.content.service.CourseBaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 课程基本信息 服务实现类
 * </p>
 *
 * @author mewp
 */
@Slf4j
@Service
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements CourseBaseService {

    @Resource
    private CourseMarketMapper courseMarketMapper;
    @Resource
    private CourseCategoryMapper courseCategoryMapper;

    @Override
    public PageResult<CourseBase> queryCourseBasePageList(PageParams pageParams, QueryCourseParamsDTO courseParamsDTO) {
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(courseParamsDTO.getCourseName()),
                CourseBase::getName, courseParamsDTO.getCourseName());
        queryWrapper.eq(StringUtils.isNotBlank(courseParamsDTO.getAuditStatus()),
                CourseBase::getAuditStatus, courseParamsDTO.getAuditStatus());
        queryWrapper.eq(StringUtils.isNotBlank(courseParamsDTO.getPublishStatus()),
                CourseBase::getStatus, courseParamsDTO.getPublishStatus());

        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        Page<CourseBase> pageInfo = this.baseMapper.selectPage(page, queryWrapper);
        return new PageResult<>(pageInfo.getTotal(), pageParams.getPageNo(), pageParams.getPageSize(), page.getRecords());
    }

    @Transactional
    @Override
    public CourseBaseInfoDTO createCourseBase(Long companyId, AddOrUpdateCourseDTO courseDTO) {
        //新增课程的基本信息
        CourseBase courseBase = PoDtoConvertMapper.INSTANCE.courseBaseDto2Po(courseDTO);
        //设置审核状态
        courseBase.setAuditStatus("202002");
        //设置发布状态
        courseBase.setStatus("203001");
        //机构id
        courseBase.setCompanyId(companyId);
        //todo：待添加创建人和修改人

        int courseBaseResult = baseMapper.insert(courseBase);
        if (courseBaseResult <= 0) {
            CustomException.cast("新增课程基本信息失败");
        }

        Long courseId = courseBase.getId();

        //向课程营销表保存课程营销信息
        CourseMarket courseMarket = PoDtoConvertMapper.INSTANCE.courseMarketDto2Po(courseDTO);
        courseMarket.setId(courseId);
        int courseMarketResult = saveCourseMarket(courseMarket);
        if (courseMarketResult <= 0) {
            CustomException.cast("保存课程营销信息失败");
        }

        //查询课程基本信息及营销信息并返回
        return getCourseBaseInfo(courseId);
    }

    /**
     * 保存课程营销信息
     *
     * @param courseMarket 课程营销信息
     * @return 保存成功的个数
     */
    private int saveCourseMarket(CourseMarket courseMarket) {
        String charge = courseMarket.getCharge();
        //收费规则为收费
        if ("201001".equals(charge)) {
            if (courseMarket.getPrice() == null || courseMarket.getPrice() <= 0) {
                CustomException.cast("课程为收费价格不能为空且必须大于0");
            }
        }
        //根据id从课程营销信息
        CourseMarket market = courseMarketMapper.selectById(courseMarket.getId());
        if (market == null) {
            return courseMarketMapper.insert(courseMarket);
        }
        return courseMarketMapper.updateById(courseMarket);
    }

    /**
     * 获取课程的基本信息（基本信息和营销信息）
     *
     * @param courseId 课程ID
     * @return 基本信息
     */
    private CourseBaseInfoDTO getCourseBaseInfo(Long courseId) {
        CourseBase courseBase = baseMapper.selectById(courseId);
        if (Objects.isNull(courseBase)) {
            return null;
        }
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        CourseBaseInfoDTO courseBaseInfoDto = PoDtoConvertMapper
                .INSTANCE.courseBaseAndMarketPo2Dto(courseBase, courseMarket);

        //查询分类名称
/*        CourseCategory courseCategorySt = courseCategoryMapper.selectById(courseBase.getSt());
        CourseCategory courseCategoryMt = courseCategoryMapper.selectById(courseBase.getMt());
        courseBaseInfoDto.setMtName(courseCategoryMt.getName());
        courseBaseInfoDto.setStName(courseCategorySt.getName());*/

        MapResultHandler<String, String> resultHandler = new MapResultHandler<>();
        courseCategoryMapper.findByIds(resultHandler, Arrays.asList(courseBase.getSt(), courseBase.getMt()));
        Map<String, String> map = resultHandler.getMappedResults();
        courseBaseInfoDto.setMtName(map.get(courseBase.getMt()));
        courseBaseInfoDto.setStName(map.get(courseBase.getSt()));

        return courseBaseInfoDto;
    }
}
