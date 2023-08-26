package com.mewp.edu.content.model.po;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 课程分类
 * </p>
 *
 * @author mewp
 */

@Data
@ApiModel(description = "课程分类")
@TableName("course_category")
public class CourseCategory implements Serializable {

    @ApiModelProperty(hidden = true)
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("课程ID")
    private String id;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String name;

    /**
     * 分类标签默认和名称一样
     */
    @ApiModelProperty("分类标签")
    private String label;

    /**
     * 父结点id（第一级的父节点是0，自关联字段id）
     */
    @ApiModelProperty("父结点ID")
    private String parentid;

    /**
     * 是否显示
     */
    @ApiModelProperty("是否显示")
    private Integer isShow;

    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    private Integer orderby;

    /**
     * 是否叶子
     */
    @ApiModelProperty("是否叶子")
    private Integer isLeaf;
}
