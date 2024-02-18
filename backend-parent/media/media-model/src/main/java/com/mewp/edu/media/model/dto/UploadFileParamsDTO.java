package com.mewp.edu.media.model.dto;

import lombok.Data;

/**
 * 上传普通文件请求参数
 *
 * @author mewp
 * @version 1.0
 * @date 2023/11/4 21:55
 */
@Data
public class UploadFileParamsDTO {
    /**
     * 文件名称
     */
    private String filename;

    /**
     * 文件类型（文档，音频，视频）
     */
    private String fileType;
    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 标签
     */
    private String tags;

    /**
     * 上传人
     */
    private String username;

    /**
     * 备注
     */
    private String remark;
}
