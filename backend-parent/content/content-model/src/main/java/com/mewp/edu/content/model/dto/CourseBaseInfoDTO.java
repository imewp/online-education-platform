package com.mewp.edu.content.model.dto;

import com.mewp.edu.content.model.po.CourseBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 课程基本信息DTO
 *
 * @author mewp
 * @version 1.0
 * @date 2023/8/26 11:28
 */
@ApiModel("课程基本信息")
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseBaseInfoDTO extends CourseBase {
    /**
     * 收费规则，对应数据字典
     */
    @ApiModelProperty(value = "收费规则")
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

    /**
     * 大分类名称
     */
    @ApiModelProperty("大分类名称")
    private String mtName;

    /**
     * 小分类名称
     */
    @ApiModelProperty("小分类名称")
    private String stName;
}
