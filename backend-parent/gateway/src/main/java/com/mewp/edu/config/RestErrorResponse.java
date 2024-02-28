package com.mewp.edu.config;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 错误响应参数包装
 *
 * @author mewp
 * @version 1.0
 * @date 2024/4/24 09:03
 */
@Setter
@Getter
public class RestErrorResponse implements Serializable {

    private String errMessage;

    public RestErrorResponse(String errMessage) {
        this.errMessage = errMessage;
    }
}
