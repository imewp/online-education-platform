package com.mewp.edu.content.model.dto;

import com.mewp.edu.content.model.po.CourseCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author mewp
 * @version 1.0
 * @date 2023/8/20 23:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "课程分类树形结构")
public class CourseCategoryTreeDTO extends CourseCategory {

    /**
     * 子节点
     */
    @ApiModelProperty("子节点")
    private List<CourseCategory> childrenTreeNodes;
}
