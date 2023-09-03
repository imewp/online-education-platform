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
 * 媒资信息
 * </p>
 *
 * @author mewp
 */
@ApiModel(description = "媒资信息")
@Data
@TableName("media_files")
public class MediaFiles implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件id,md5值
     */
    @ApiModelProperty(value = "文件ID", dataType = "string")
    private String id;

    /**
     * 机构ID
     */
    @ApiModelProperty(value = "机构ID", dataType = "long")
    private Long companyId;

    /**
     * 机构名称
     */
    @ApiModelProperty(value = "机构名称", dataType = "string")
    private String companyName;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称", dataType = "string")
    private String filename;

    /**
     * 文件类型（图片、文档、视频）
     */
    @ApiModelProperty(value = "文件类型（图片、文档、视频）", dataType = "string")
    private String fileType;

    /**
     * 标签
     */
    @ApiModelProperty(value = "标签", dataType = "string")
    private String tags;

    /**
     * 存储目录
     */
    @ApiModelProperty(value = "存储目录", dataType = "string")
    private String bucket;

    /**
     * 存储路径
     */
    @ApiModelProperty(value = "存储路径", dataType = "string")
    private String filePath;

    /**
     * 文件id
     */
    @ApiModelProperty(value = "文件ID", dataType = "string")
    private String fileId;

    /**
     * 媒资文件访问地址
     */
    @ApiModelProperty(value = "媒资文件访问地址", dataType = "string")
    private String url;

    /**
     * 上传人
     */
    @ApiModelProperty(value = "上传人", dataType = "string")
    private String username;

    /**
     * 上传时间
     */
    @ApiModelProperty(value = "上传时间", dataType = "date")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "机构ID", dataType = "date")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime changeDate;

    /**
     * 状态,1:正常，0:不展示
     */
    @ApiModelProperty(value = "状态,1:正常，0:不展示", dataType = "string")
    private String status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", dataType = "string")
    private String remark;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态", dataType = "string")
    private String auditStatus;

    /**
     * 审核意见
     */
    @ApiModelProperty(value = "审核意见", dataType = "string")
    private String auditMind;

    /**
     * 文件大小
     */
    @ApiModelProperty(value = "文件大小", dataType = "long")
    private Long fileSize;
}
