package com.mewp.edu.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mewp.edu.common.model.PageResult;
import com.mewp.edu.common.param.PageParams;
import com.mewp.edu.content.mapper.CourseBaseMapper;
import com.mewp.edu.content.model.dto.QueryCourseParamsDTO;
import com.mewp.edu.content.model.po.CourseBase;
import com.mewp.edu.content.service.CourseBaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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
}
