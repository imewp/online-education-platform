package com.mewp.edu.common.exception;

import java.io.Serializable;

/**
 * 异常信息
 *
 * @author mewp
 * @version 1.0
 * @date 2023/8/19 16:03
 */
public class RestErrorResponse implements Serializable {
    private String errorMessage;

    public RestErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
