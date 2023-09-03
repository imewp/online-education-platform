package com.mewp.edu.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加/修改课程教师信息
 *
 * @author mewp
 * @version 1.0
 * @date 2023/8/28 18:58
 */
@Data
@Valid
@ApiModel(value = "AddOrUpdateCourseTeacherDTO", description = "添加或修改课程教师信息")
public class AddOrUpdateCourseTeacherDTO {
    @ApiModelProperty("教师ID")
    private Long id;

    /**
     * 课程标识
     */
    @ApiModelProperty(value = "课程标识", required = true)
    @NotNull(message = "课程标识不能为空")
    private Long courseId;

    /**
     * 教师标识
     */
    @ApiModelProperty("教师标识")
    @NotBlank(message = "教师标志不能为空")
    private String teacherName;

    /**
     * 教师职位
     */
    @ApiModelProperty("教师职位")
    private String position;

    /**
     * 教师简介
     */
    @ApiModelProperty("教师简介")
    private String introduction;

    /**
     * 照片
     */
    @ApiModelProperty("照片")
    private String photograph;
}
