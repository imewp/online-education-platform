package com.mewp.edu.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页查询通用参数
 *
 * @author mewp
 * @version 1.0
 * @date 2023/8/14 23:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "分页参数")
public class PageParams {
    /**
     * 默认起始页码
     */
    public static final long DEFAULT_PAGE_CURRENT = 1L;

    /**
     * 默认每页记录数
     */
    public static final long DEFAULT_PAGE_SIZE = 10L;

    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码", required = true)
    private Long pageNo = DEFAULT_PAGE_CURRENT;

    /**
     * 当前每页记录数
     */
    @ApiModelProperty(value = "每页记录数", required = true)
    private Long pageSize = DEFAULT_PAGE_SIZE;
}
