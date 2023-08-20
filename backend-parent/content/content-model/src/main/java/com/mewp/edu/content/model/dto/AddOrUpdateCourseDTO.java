package com.mewp.edu.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 添加/修改课程DTO
 *
 * @author mewp
 * @version 1.0
 * @date 2023/8/19 16:28
 */
@Data
@ApiModel(value = "AddOrUpdateCourseDTO", description = "添加或修改课程基本信息")
public class AddOrUpdateCourseDTO {
    /**
     * 课程名称
     */
    @NotBlank(message = "课程名称不能为空")
    @ApiModelProperty(value = "课程名称", required = true)
    private String name;

    /**
     * 适用人群
     */
    @NotBlank(message = "适用人群不能为空")
    @Size(message = "适用人群内容过少", min = 10)
    @ApiModelProperty(value = "适用人群", required = true)
    private String users;

    /**
     * 课程标签
     */
    @ApiModelProperty(value = "课程标签")
    private String tags;

    /**
     * 大分类
     */
    @NotBlank(message = "课程分类不能为空")
    @ApiModelProperty(value = "大分类", required = true)
    private String mt;

    /**
     * 小分类
     */
    @NotBlank(message = "课程分类不能为空")
    @ApiModelProperty(value = "小分类", required = true)
    private String st;

    /**
     * 课程等级
     */
    @NotBlank(message = "课程等级不能为空")
    @ApiModelProperty(value = "课程等级", required = true)
    private String grade;

    /**
     * 教育模式(common普通，record 录播，live直播等）
     */
    @ApiModelProperty(value = "教育模式(普通，录播，直播等）", required = true)
    private String teachmode;

    /**
     * 课程介绍
     */
    @ApiModelProperty(value = "课程介绍")
    private String description;

    /**
     * 课程图片
     */
    @ApiModelProperty(value = "课程图片")
    private String pic;

    /**
     * 收费规则，对应数据字典
     */
    @NotBlank(message = "收费规则不能为空")
    @ApiModelProperty(value = "收费规则，对应数据字典", required = true)
    private String charge;

    /**
     * 现价
     */
    @ApiModelProperty(value = "现价")
    private Float price;

    /**
     * 原价
     */
    @ApiModelProperty(value = "原价")
    private Float originalPrice;

    /**
     * 咨询qq
     */
    @ApiModelProperty(value = "qq")
    private String qq;

    /**
     * 微信
     */
    @ApiModelProperty(value = "微信")
    private String wechat;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private String phone;

    /**
     * 有效期天数
     */
    @ApiModelProperty(value = "有效期")
    private Integer validDays;
}
