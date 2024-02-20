package com.mewp.edu.media.api;

import com.mewp.edu.common.exception.CustomException;
import com.mewp.edu.common.model.ResponseResult;
import com.mewp.edu.common.utils.StringUtil;
import com.mewp.edu.media.model.po.MediaFiles;
import com.mewp.edu.media.service.MediaFilesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 文件预览展示接口
 *
 * @author mewp
 * @version 1.0
 * @date 2024/2/18 19:35
 */
@Slf4j
@Api(value = "文件预览展示接口", tags = "文件预览展示接口")
@RestController
public class MediaOpenController {
    private final MediaFilesService mediaFilesService;

    public MediaOpenController(MediaFilesService mediaFilesService) {
        this.mediaFilesService = mediaFilesService;
    }

    @ApiOperation("预览文件")
    @ApiImplicitParam(name = "filedata", value = "媒体ID", required = true, dataType = "string")
    @GetMapping("/preview/{mediaId}")
    public ResponseResult<String> getMediaUrl(@PathVariable("mediaId") String mediaId) {
        MediaFiles mediaFile = mediaFilesService.getFileById(mediaId);
        if (Objects.isNull(mediaFile) || StringUtil.isEmpty(mediaFile.getUrl())) {
            CustomException.cast("媒资文件还没有转码处理");
        }
        return ResponseResult.success(mediaFile.getUrl());
    }
}
