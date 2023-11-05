package com.mewp.edu.media.api;

import com.mewp.edu.common.model.PageResult;
import com.mewp.edu.common.param.PageParams;
import com.mewp.edu.media.model.dto.QueryMediaParamsDTO;
import com.mewp.edu.media.model.dto.UploadFileParamsDTO;
import com.mewp.edu.media.model.dto.UploadFileResultDTO;
import com.mewp.edu.media.model.po.MediaFiles;
import com.mewp.edu.media.service.MediaFilesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 * 媒资信息 前端控制器
 * </p>
 *
 * @author mewp
 */
@Slf4j
@Api(value = "媒资文件管理接口", tags = "媒资文件管理接口")
@RestController
public class MediaFilesController {
    private final MediaFilesService mediaFilesService;

    public MediaFilesController(MediaFilesService mediaFilesService) {
        this.mediaFilesService = mediaFilesService;
    }

    @ApiOperation("媒资列表查询")
    @PostMapping("/files")
    public PageResult<MediaFiles> list(PageParams pageParams, @RequestBody QueryMediaParamsDTO queryMediaParamsDTO) {
        Long companyId = 1232141425L;
        return mediaFilesService.queryMediaFiles(companyId, pageParams, queryMediaParamsDTO);
    }

    @ApiOperation("上传图片")
    @PostMapping(value = "/upload/coursefile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResultDTO upload(@RequestPart("filedata") MultipartFile upload) throws IOException {
        Long companyId = 1232141425L;
        UploadFileParamsDTO fileParamsDTO = new UploadFileParamsDTO();
        //文件大小
        fileParamsDTO.setFileSize(upload.getSize());
        //文件类型 图片
        fileParamsDTO.setFileType("001001");
        //文件名
        fileParamsDTO.setFilename(upload.getOriginalFilename());
        //创建临时文件
        File tempFile = File.createTempFile("minio", "temp");
        //上传的文件拷贝到临时文件
        upload.transferTo(tempFile);
        //文件路径
        String absolutePath = tempFile.getAbsolutePath();
        //上传文件
        return mediaFilesService.uploadFile(companyId, fileParamsDTO, absolutePath);
    }
}
