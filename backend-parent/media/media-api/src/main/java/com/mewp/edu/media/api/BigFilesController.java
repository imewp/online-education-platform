package com.mewp.edu.media.api;

import com.mewp.edu.common.model.ResponseResult;
import com.mewp.edu.media.model.dto.UploadFileParamsDTO;
import com.mewp.edu.media.service.MediaFilesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 大文件上传接口
 *
 * @author mewp
 * @version 1.0
 * @date 2023/12/17 14:29
 */
@Api(value = "大文件上传", tags = "大文件上传接口")
@RestController
@RequestMapping("/upload")
public class BigFilesController {

    private final MediaFilesService mediaFilesService;

    public BigFilesController(MediaFilesService mediaFilesService) {
        this.mediaFilesService = mediaFilesService;
    }

    @ApiOperation("文件上传前检查文件")
    @ApiImplicitParam(name = "fileMd5", value = "文件md5", required = true, dataType = "string")
    @PostMapping("/checkfile")
    public ResponseResult<Boolean> checkFile(@RequestParam("fileMd5") String fileMd5) {
        return mediaFilesService.checkFile(fileMd5);
    }

    @ApiOperation("分块文件上传前检查")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileMd5", value = "文件md5", required = true, dataType = "string"),
            @ApiImplicitParam(name = "chunk", value = "分块序号", required = true, dataType = "int")
    })
    @PostMapping("/checkchunk")
    public ResponseResult<Boolean> checkFile(@RequestParam("fileMd5") String fileMd5,
                                             @RequestParam("chunk") Integer chunkIndex) {
        return mediaFilesService.checkChunk(fileMd5, chunkIndex);
    }

    @ApiOperation("上传分块文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "MultipartFile"),
            @ApiImplicitParam(name = "fileMd5", value = "文件MD5", required = true, dataType = "string"),
            @ApiImplicitParam(name = "chunk", value = "分块序号", required = true, dataType = "int")
    })
    @PostMapping("/uploadchunk")
    public ResponseResult<Boolean> uploadChunk(@RequestParam("file") MultipartFile file,
                                               @RequestParam("fileMd5") String fileMd5,
                                               @RequestParam("chunk") Integer chunkIndex) throws IOException {
        //创建临时文件
        File tempFile = File.createTempFile("minio", ".temp");
        //上传的文件拷贝到临时文件
        file.transferTo(tempFile);
        return mediaFilesService.uploadChunk(fileMd5, chunkIndex, tempFile.getAbsolutePath());
    }

    @ApiOperation("合并文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileMd5", value = "文件MD5", required = true, dataType = "string"),
            @ApiImplicitParam(name = "fileName", value = "文件名", required = true, dataType = "string"),
            @ApiImplicitParam(name = "chunkTotal", value = "分块总数", required = true, dataType = "int")
    })
    @PostMapping("/mergechunks")
    public ResponseResult<Boolean> mergeChunks(@RequestParam("fileMd5") String fileMd5,
                                               @RequestParam("fileName") String fileName,
                                               @RequestParam("chunkTotal") Integer chunkTotal) {
        Long companyId = 1232141425L;
        UploadFileParamsDTO paramsDTO = new UploadFileParamsDTO();
        paramsDTO.setFileType("001002");
        paramsDTO.setTags("课程视频");
        paramsDTO.setRemark("");
        paramsDTO.setFilename(fileName);
        return mediaFilesService.mergeChunks(companyId, fileMd5, chunkTotal, paramsDTO);
    }
}

