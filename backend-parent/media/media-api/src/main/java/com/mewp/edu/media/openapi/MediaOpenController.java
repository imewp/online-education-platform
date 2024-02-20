package com.mewp.edu.media.openapi;

import com.mewp.edu.common.exception.CustomException;
import com.mewp.edu.common.model.ResponseResult;
import com.mewp.edu.common.utils.StringUtil;
import com.mewp.edu.media.model.po.MediaFiles;
import com.mewp.edu.media.service.MediaFilesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author mewp
 * @version 1.0
 * @date 2024/2/20 21:02
 */
@Api(value = "MediaOpenController", tags = "公开媒资文件管理接口")
@RestController
@RequestMapping("/open")
public class MediaOpenController {
    private final MediaFilesService mediaFilesService;

    public MediaOpenController(MediaFilesService mediaFilesService) {
        this.mediaFilesService = mediaFilesService;
    }

    @ApiOperation("媒资文件预览")
    @ApiImplicitParam(name = "mediaId", value = "媒体ID", required = true, dataType = "string")
    @GetMapping("/preview/{mediaId}")
    public ResponseResult<String> getMediaUrl(@PathVariable("mediaId") String mediaId) {
        MediaFiles mediaFile = mediaFilesService.getFileById(mediaId);
        if (Objects.isNull(mediaFile) || StringUtil.isBlank(mediaFile.getUrl())) {
            CustomException.cast("媒资文件还没有转码处理");
        }
        return ResponseResult.success(null, mediaFile.getUrl());
    }
}
