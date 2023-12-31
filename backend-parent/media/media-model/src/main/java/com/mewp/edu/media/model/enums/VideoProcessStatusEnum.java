package com.mewp.edu.media.model.enums;

import lombok.Getter;

/**
 * @author mewp
 * @version 1.0
 * @date 2023/12/31 17:15
 */
@Getter
public enum VideoProcessStatusEnum {
    UNPROCESSED("1", "未处理"),
    SUCCESSFULLY_PROCESSED("2", "处理成功"),
    PROCESSING_FAILED("3", "处理失败"),
    PROCESSING("4", "处理中");

    private final String code;
    private final String name;

    VideoProcessStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
