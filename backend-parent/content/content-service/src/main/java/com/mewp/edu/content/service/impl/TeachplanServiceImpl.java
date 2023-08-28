package com.mewp.edu.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mewp.edu.common.exception.CustomException;
import com.mewp.edu.content.mapper.TeachplanMapper;
import com.mewp.edu.content.model.converter.PoDtoConvertMapper;
import com.mewp.edu.content.model.dto.AddOrUpdateTeachPlanDTO;
import com.mewp.edu.content.model.dto.TeachPlanDTO;
import com.mewp.edu.content.model.po.CourseBase;
import com.mewp.edu.content.model.po.Teachplan;
import com.mewp.edu.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 课程计划 服务实现类
 * </p>
 *
 * @author mewp
 */
@Slf4j
@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan> implements TeachplanService {

    @Override
    public List<TeachPlanDTO> findTeachPlanTree(Long courseId) {
        return baseMapper.selectTreeNodes(courseId);
    }

    @Override
    public void saveOrUpdatePlan(AddOrUpdateTeachPlanDTO teacherPlanDTO) {
        Long planId = teacherPlanDTO.getId();
        Teachplan teachplan = PoDtoConvertMapper.INSTANCE.teachPlanDto2Po(teacherPlanDTO);
        //计划id为空，则为新增操作
        if (Objects.isNull(planId)) {
            //取出同父同级别的课程计划数量
            int count = getTeachPlanCount(teacherPlanDTO.getCourseId(), teacherPlanDTO.getParentid());
            //设置排序号
            teachplan.setOrderby(count + 1);
            baseMapper.insert(teachplan);
        } else {
            //计划id不为空，则为修改操作
            Teachplan teachPlanTemp = baseMapper.selectById(planId);
            if (Objects.isNull(teachPlanTemp)) {
                CustomException.cast("该课程不存在");
            }
            baseMapper.updateById(teachplan);
        }
    }

    /**
     * 获取最新的排序号
     *
     * @param courseId 课程ID
     * @param parentId 父课程计划ID
     * @return 最新排序号
     */
    private int getTeachPlanCount(Long courseId, long parentId) {
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId, courseId)
                .eq(Teachplan::getParentid, parentId);
        return baseMapper.selectCount(queryWrapper);
    }
}
