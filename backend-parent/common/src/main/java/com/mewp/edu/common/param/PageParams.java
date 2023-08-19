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
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码", required = true)
    private Long pageNo = 1L;

    /**
     * 每页记录数默认值
     */
    @ApiModelProperty(value = "每页记录数", required = true)
    private Long pageSize = 10L;
}
