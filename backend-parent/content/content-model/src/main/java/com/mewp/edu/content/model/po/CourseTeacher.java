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
 * 课程-教师关系表
 * </p>
 *
 * @author mewp
 */
@Data
@TableName("course_teacher")
@ApiModel(description = "课程教师关联信息")
public class CourseTeacher implements Serializable {
    private static final long serialVersionUID = 141360336197361368L;

    /**
     * 主键
     */
    @ApiModelProperty("教师ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程标识
     */
    @ApiModelProperty("课程标识")
    private Long courseId;

    /**
     * 教师标识
     */
    @ApiModelProperty("教师标识")
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

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;
}
