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
 * 课程计划与媒资关系
 * </p>
 *
 * @author mewp
 */
@Data
@ApiModel(description = "课程计划与媒资关系")
@TableName("teachplan_media")
public class TeachplanMedia implements Serializable {

    private static final long serialVersionUID = -2309371308217062490L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 媒资文件id
     */
    @ApiModelProperty("媒资文件ID")
    private String mediaId;

    /**
     * 课程计划标识
     */
    @ApiModelProperty("课程计划标识")
    private Long teachplanId;

    /**
     * 课程标识
     */
    @ApiModelProperty("课程标识")
    private Long courseId;

    /**
     * 媒资文件原始名称
     */
    @ApiModelProperty("媒资文件原始名称")
    @TableField("media_fileName")
    private String mediaFilename;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createPeople;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String changePeople;
}
