package com.mewp.edu.media.model.dto;

import com.mewp.edu.media.model.po.MediaFiles;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 上传普通文件成功响应结果
 *
 * @author mewp
 * @version 1.0
 * @date 2023/11/4 21:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UploadFileResultDTO extends MediaFiles {

}
