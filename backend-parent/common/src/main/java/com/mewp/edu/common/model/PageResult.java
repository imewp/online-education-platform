package com.mewp.edu.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果模型类
 *
 * @author mewp
 * @version 1.0
 * @date 2023/8/14 23:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "分页结果")
public class PageResult<T> implements Serializable {
    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总记录数", dataType = "long")
    private Long counts;

    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码", dataType = "long")
    private Long page;

    /**
     * 每页记录数
     */
    @ApiModelProperty(value = "每页记录数", dataType = "long")
    private Long pageSize;

    /**
     * 数据列表
     */
    @ApiModelProperty(value = "数据列表", dataType = "list")
    private List<T> items;
}
