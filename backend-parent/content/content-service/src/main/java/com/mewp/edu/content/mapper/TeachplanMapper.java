package com.mewp.edu.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mewp.edu.content.model.dto.TeachPlanDTO;
import com.mewp.edu.content.model.po.Teachplan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程计划 Mapper 接口
 * </p>
 *
 * @author mewp
 */
public interface TeachplanMapper extends BaseMapper<Teachplan> {

    /**
     * 查询某课程的课程计划，组成树形结构
     *
     * @param courseId 课程ID
     * @return 树形结构
     */
    List<TeachPlanDTO> selectTreeNodes(@Param("courseId") Long courseId);
}
