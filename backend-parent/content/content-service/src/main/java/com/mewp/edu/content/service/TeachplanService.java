package com.mewp.edu.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mewp.edu.content.model.dto.AddOrUpdateTeachPlanDTO;
import com.mewp.edu.content.model.dto.TeachPlanDTO;
import com.mewp.edu.content.model.po.Teachplan;

import java.util.List;

/**
 * <p>
 * 课程计划 服务类
 * </p>
 *
 * @author mewp
 * @since 2023-08-20
 */
public interface TeachplanService extends IService<Teachplan> {

    /**
     * 查询某课程的课程计划，组成树形结构
     *
     * @param courseId 课程ID
     * @return 树形结构
     */
    List<TeachPlanDTO> findTeachPlanTree(Long courseId);

    /**
     * 添加或修改课程计划
     *
     * @param teacherPlanDTO 课程计划
     */
    void saveOrUpdatePlan(AddOrUpdateTeachPlanDTO teacherPlanDTO);

    /**
     * 删除课程计划
     *
     * @param id 计划ID
     */
    void deleteTeachPlan(Long id);

    /**
     * 改变课程计划的排序
     *
     * @param moveType 移动类型
     * @param id       计划ID
     */
    void moveTeachPlan(String moveType, Long id);
}
