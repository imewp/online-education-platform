package com.mewp.edu.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 *
 * @author mewp
 * @version 1.0
 * @date 2023/8/19 16:02
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse customException(CustomException e) {
        log.error("系统异常：{}", e.getErrMessage(), e);
        //解析异常信息
        return new RestErrorResponse(e.getErrMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse exception(Exception e) {
        log.error("系统异常：{}", e.getMessage(), e);
        //解析异常信息
        return new RestErrorResponse(CommonError.UNKNOWN_ERROR.getErrMessage());
    }
}
