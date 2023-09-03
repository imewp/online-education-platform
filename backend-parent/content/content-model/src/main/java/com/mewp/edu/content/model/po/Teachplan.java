package com.mewp.edu.content.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 课程计划
 * </p>
 *
 * @author mewp
 */
@ApiModel(description = "课程计划")
@Data
@TableName("teachplan")
public class Teachplan implements Serializable {
    private static final long serialVersionUID = 1595018176093812658L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程计划名称
     */
    @ApiModelProperty("课程计划名称")
    private String pname;

    /**
     * 课程计划父级Id
     */
    @ApiModelProperty("课程计划父级ID")
    private Long parentid;

    /**
     * 层级，分为1、2、3级
     */
    @ApiModelProperty("层级，分为1、2、3级")
    private Integer grade;

    /**
     * 课程类型:1视频、2文档
     */
    @ApiModelProperty("课程类型:1视频、2文档")
    private String mediaType;

    /**
     * 开始直播时间
     */
    @ApiModelProperty("开始直播时间")
    private LocalDateTime startTime;

    /**
     * 直播结束时间
     */
    @ApiModelProperty("直播结束时间")
    private LocalDateTime endTime;

    /**
     * 章节及课程时介绍
     */
    @ApiModelProperty("章节及课程时介绍")
    private String description;

    /**
     * 时长，单位时:分:秒
     */
    @ApiModelProperty("时长，单位时:分:秒")
    private String timelength;

    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    private Integer orderby;

    /**
     * 课程标识
     */
    @ApiModelProperty("课程标识")
    private Long courseId;

    /**
     * 课程发布标识
     */
    @ApiModelProperty("课程发布标识")
    private Long coursePubId;

    /**
     * 状态（1正常  0删除）
     */
    @ApiModelProperty("状态（1正常  0删除）")
    private Integer status;

    /**
     * 是否支持试学或预览（试看）
     */
    @ApiModelProperty("是否支持试学或预览（试看）")
    private String isPreview;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime changeDate;
}
