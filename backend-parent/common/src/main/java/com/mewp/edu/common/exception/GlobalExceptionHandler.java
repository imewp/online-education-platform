package com.mewp.edu.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        //获取发生异常的类和方法
        StackTraceElement stackTraceElement = e.getStackTrace()[1];
        String className = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();
        log.error("系统异常：【{}】，发生异常的类：【{}】，方法：【{}】", e.getErrMessage(), className, methodName);
        log.error(e.getMessage(), e);
        //解析异常信息
        return new RestErrorResponse(e.getErrMessage());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, ConstraintViolationException.class,
            BindException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestErrorResponse handleException(Exception e) {
        String msg = "";
        //缺少参数
        if (e instanceof MissingServletRequestParameterException) {
            String exMsg = ((MissingServletRequestParameterException) e).getParameterName();
            log.error("缺少参数异常：" + e.getMessage());
            msg = MessageFormat.format("缺少参数{0}", exMsg);
            return new RestErrorResponse(msg);
        } else if (e instanceof ConstraintViolationException) {
            // 单个参数校验异常
            log.error("单个参数校验异常：" + e.getMessage());
            Set<ConstraintViolation<?>> sets = ((ConstraintViolationException) e).getConstraintViolations();
            if (!CollectionUtils.isEmpty(sets)) {
                StringBuilder sb = new StringBuilder();
                sets.forEach(error -> {
                    if (error instanceof FieldError) {
                        sb.append(((FieldError) error).getField()).append(":");
                    }
                    sb.append(error.getMessage()).append(";");
                });
                msg = sb.toString();
                msg = StringUtils.substring(msg, 0, msg.length() - 1);
            }
        } else if (e instanceof BindException) {
            // GET请求的对象参数校验异常
            log.error("GET请求参数校验异常：" + e.getMessage());
            List<ObjectError> errors = ((BindException) e).getBindingResult().getAllErrors();
            msg = getValidExceptionMsg(errors);
        } else if (e instanceof MethodArgumentNotValidException) {
            // POST请求的对象参数校验异常
            log.error("POST请求参数校验异常：" + e.getMessage());
            List<ObjectError> errors = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors();
            msg = getValidExceptionMsg(errors);
        }
        return new RestErrorResponse(msg);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse exception(Exception e) {
        log.error("系统异常：{}", e.getMessage(), e);
        //解析异常信息
        return new RestErrorResponse(CommonError.UNKNOWN_ERROR.getErrMessage());
    }

    /**
     * 组合异常信息
     *
     * @param errors 异常信息列表
     * @return 异常信息字符串
     */
    private String getValidExceptionMsg(List<ObjectError> errors) {
        ArrayList<String> msgList = new ArrayList<>();
        errors.forEach(item -> msgList.add(item.getDefaultMessage()));
        return StringUtils.join(msgList, ",");
    }
}
