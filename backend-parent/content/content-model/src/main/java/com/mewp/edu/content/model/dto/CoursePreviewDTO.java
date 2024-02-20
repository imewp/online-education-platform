package com.mewp.edu.content.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author mewp
 * @version 1.0
 * @date 2024/2/20 16:38
 */
@Data
@ApiModel(value = "CoursePreviewDTO", description = "课程预览数据模型")
public class CoursePreviewDTO {
    /**
     * 课程基本信息，课程营销信息
     */
    private CourseBaseInfoDTO courseBase;

    /**
     * 课程计划信息
     */
    private List<TeachPlanDTO> teachPlans;
}
