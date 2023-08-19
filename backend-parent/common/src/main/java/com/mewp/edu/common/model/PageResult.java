package com.mewp.edu.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

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
@AllArgsConstructor
public class PageResult<T> implements Serializable {
    /**
     * 总记录数
     */
    private Long count;

    /**
     * 当前页码
     */
    private Long page;

    /**
     * 每页记录数
     */
    private Long pageSize;

    /**
     * 数据列表
     */
    private List<T> list;
}
