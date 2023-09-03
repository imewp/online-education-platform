package com.mewp.edu.media.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 视频处理历史
 * </p>
 *
 * @author mewp
 */
@ApiModel(description = "视频处理历史")
@Data
@TableName("media_process_history")
public class MediaProcessHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", dataType = "int")
    private Long id;

    /**
     * 文件标识
     */
    @ApiModelProperty(value = "文件标识", dataType = "string")
    private String fileId;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称", dataType = "string")
    private String filename;

    /**
     * 存储桶
     */
    @ApiModelProperty(value = "存储桶", dataType = "string")
    private String bucket;

    /**
     * 存储路径
     */
    @ApiModelProperty(value = "存储路径", dataType = "string")
    private String filePath;

    /**
     * 状态,1:未处理，2：处理成功  3处理失败
     */
    @ApiModelProperty(value = "状态,1:未处理，2：处理成功  3处理失败", dataType = "string")
    private String status;

    /**
     * 上传时间
     */
    @ApiModelProperty(value = "上传时间", dataType = "date")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 完成时间
     */
    @ApiModelProperty(value = "完成时间", dataType = "date")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finishDate;

    /**
     * 媒资文件访问地址
     */
    @ApiModelProperty(value = "媒资文件访问地址", dataType = "string")
    private String url;

    /**
     * 失败原因
     */
    @ApiModelProperty(value = "失败原因", dataType = "string")
    private String errormsg;
}
