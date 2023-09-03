package com.mewp.edu.media.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 媒资文件查询请求参数
 *
 * @author mewp
 * @version 1.0
 * @date 2023/9/3 10:36
 */
@ApiModel(description = "媒资文件查询请求参数")
@Data
public class QueryMediaParamsDTO {
    @ApiModelProperty("媒资文件名称")
    private String filename;

    @ApiModelProperty("媒资类型")
    private String fileType;

    @ApiModelProperty("审核状态")
    private String auditStatus;
}
