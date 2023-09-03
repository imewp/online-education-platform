package com.mewp.edu.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加/修改课程计划DTO
 *
 * @author mewp
 * @version 1.0
 * @date 2023/8/28 00:45
 */
@Data
@Valid
@ApiModel(value = "AddOrUpdateTeachPlanDTO", description = "添加或修改课程计划")
public class AddOrUpdateTeachPlanDTO {
    /**
     * 课程计划ID
     */
    @ApiModelProperty(value = "课程计划ID")
    private Long id;

    /**
     * 课程计划名称
     */
    @ApiModelProperty(value = "课程计划名称", required = true)
    @NotBlank(message = "课程计划名称不能为空")
    private String pname;

    /**
     * 课程计划父级Id
     */
    @ApiModelProperty(value = "课程计划父级ID", required = true)
    @NotNull(message = "课程计划父级Id不能为空")
    private Long parentid;

    /**
     * 层级，分为1、2、3级
     */
    @ApiModelProperty(value = "层级，分为1、2、3级", required = true)
    @NotNull(message = "层级不能为空")
    private Integer grade;

    /**
     * 课程类型:1视频、2文档
     */
    @ApiModelProperty("课程类型:1视频、2文档")
    private String mediaType;

    /**
     * 课程标识
     */
    @ApiModelProperty(value = "课程标识", required = true)
    @NotNull(message = "课程标识不能为空")
    private Long courseId;

    /**
     * 课程发布标识
     */
    @ApiModelProperty("课程发布标识")
    private Long coursePubId;

    /**
     * 是否支持试学或预览（试看）
     */
    @ApiModelProperty("是否支持试学或预览（试看）")
    private String isPreview;
}
