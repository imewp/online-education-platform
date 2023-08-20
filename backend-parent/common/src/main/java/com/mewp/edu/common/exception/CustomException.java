package com.mewp.edu.common.exception;

/**
 * @author mewp
 * @version 1.0
 * @date 2023/8/19 15:56
 */
public class CustomException extends RuntimeException {
    private String errMessage;

    public CustomException() {
    }

    public CustomException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public static void cast(String message) {
        throw new CustomException(message);
    }

    public static void cast(CommonError error) {
        throw new CustomException(error.getErrMessage());
    }
}
