package com.mewp.edu.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常类
 *
 * @author mewp
 * @version 1.0
 * @date 2023/8/19 15:56
 */
@Getter
@Setter
public class CustomException extends RuntimeException {
    /**
     * 异常信息
     */
    private String errMessage;

    /**
     * 无参构造函数
     */
    public CustomException() {
    }

    /**
     * 构造函数
     *
     * @param errMessage 异常信息
     */
    public CustomException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
    }

    /**
     * 抛出一个自定义异常
     *
     * @param message 异常信息，用于描述发生异常的原因或情况
     * @throws CustomException 抛出一个CustomException异常
     */
    public static void cast(String message) {
        throw new CustomException(message);
    }

    /**
     * 抛出一个自定义异常
     *
     * @param error 错误枚举
     * @throws CustomException 抛出一个CustomException异常
     */
    public static void cast(CommonError error) {
        throw new CustomException(error.getErrMessage());
    }
}
