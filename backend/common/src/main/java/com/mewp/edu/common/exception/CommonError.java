package com.mewp.edu.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用错误信息
 *
 * @author mewp
 * @version 1.0
 * @date 2023/8/19 11:53
 */
@Getter
@AllArgsConstructor
public enum CommonError {
    UNKNOWN_ERROR("执行过程异常，请重试！"),
    PARAMS_ERROR("非法参数"),
    OBJECT_NULL("对象为空"),
    QUERY_NULL("查询结果为空"),
    REQUEST_NULL("请求参数为空");

    private String errMessage;


}
