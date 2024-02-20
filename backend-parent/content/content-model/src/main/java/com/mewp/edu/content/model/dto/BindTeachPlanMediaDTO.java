package com.mewp.edu.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author mewp
 * @version 1.0
 * @date 2024/1/22 11:54
 */
@ApiModel(value = "BindTeachPlanMediaDTO", description = "教学计划-媒资绑定提交数据")
@Valid
@Data
public class BindTeachPlanMediaDTO {
    @ApiModelProperty(value = "媒资文件ID", required = true)
    @NotBlank(message = "媒资文件ID不能为空")
    private String mediaId;

    @ApiModelProperty(value = "媒资文件名称", required = true)
    @NotBlank(message = "媒资文件名称不能为空")
    private String fileName;

    @ApiModelProperty(value = "课程计划标识", required = true)
    @NotNull(message = "课程计划标识不能为空")
    private Long teachplanId;
}
