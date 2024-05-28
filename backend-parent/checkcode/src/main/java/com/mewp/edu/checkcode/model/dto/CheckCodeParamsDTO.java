package com.mewp.edu.checkcode.model.dto;

import lombok.Data;

/**
 * 验证码生成参数类
 *
 * @author mewp
 * @version 1.0
 * @date 2024/5/28 00:05
 */
@Data
public class CheckCodeParamsDTO {
    /**
     * 验证码类型:pic、sms、email等
     */
    private String checkCodeType;

    /**
     * 业务携带参数
     */
    private String param1;
    private String param2;
    private String param3;
}
