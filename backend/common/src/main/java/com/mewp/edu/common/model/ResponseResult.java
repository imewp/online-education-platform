package com.mewp.edu.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 响应结果
 *
 * @author mewp
 * @version 1.0
 * @date 2023/8/19 23:09
 */
@ApiModel(description = "响应结果")
@Data
public class ResponseResult<T> {
    /**
     * 编码 0正常 -1错误
     */
    @ApiModelProperty(value = "编码", dataType = "int")
    private Integer code;

    /**
     * 响应提示信息
     */
    @ApiModelProperty(value = "响应提示信息", dataType = "string")
    private String msg;

    /**
     * 响应内容
     */
    @ApiModelProperty(value = "响应内容")
    private T data;

    public ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult() {
        this(0, "success");
    }

    /**
     * 错误信息的封装
     *
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> fail() {
        return fail(null);
    }

    /**
     * 错误信息的封装
     *
     * @param <T> 数据类型
     * @param msg 提示信息
     * @return 响应结果
     */
    public static <T> ResponseResult<T> fail(String msg) {
        return fail(msg, null);
    }

    /**
     * 错误信息的封装
     *
     * @param <T>  数据类型
     * @param msg  提示信息
     * @param data 数据
     * @return 响应结果
     */
    public static <T> ResponseResult<T> fail(String msg, T data) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(-1);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    /**
     * 正常信息的封装
     *
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>();
    }

    /**
     * 正常信息的封装
     *
     * @param <T> 数据类型
     * @param msg 提示信息
     * @return 响应结果
     */
    public static <T> ResponseResult<T> success(String msg) {
        return success(msg, null);
    }

    /**
     * 正常信息的封装
     *
     * @param <T>  数据类型
     * @param msg  提示信息
     * @param data 数据
     * @return 响应结果
     */
    public static <T> ResponseResult<T> success(String msg, T data) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
