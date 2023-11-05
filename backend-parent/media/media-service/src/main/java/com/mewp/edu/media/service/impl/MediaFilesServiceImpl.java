package com.mewp.edu.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.mewp.edu.common.exception.CustomException;
import com.mewp.edu.common.model.PageResult;
import com.mewp.edu.common.param.PageParams;
import com.mewp.edu.common.utils.DateUtil;
import com.mewp.edu.media.mapper.MediaFilesMapper;
import com.mewp.edu.media.model.converter.PoDtoConvertMapper;
import com.mewp.edu.media.model.dto.QueryMediaParamsDTO;
import com.mewp.edu.media.model.dto.UploadFileParamsDTO;
import com.mewp.edu.media.model.dto.UploadFileResultDTO;
import com.mewp.edu.media.model.po.MediaFiles;
import com.mewp.edu.media.service.MediaFilesService;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 媒资信息 服务实现类
 * </p>
 *
 * @author mewp
 */
@Slf4j
@Service
public class MediaFilesServiceImpl extends ServiceImpl<MediaFilesMapper, MediaFiles> implements MediaFilesService {

    private final MinioClient minioClient;

    private final MediaFilesMapper mediaFilesMapper;

    /**
     * 代理对象
     */
    @Autowired
    private MediaFilesService currentProxy;

    /**
     * 普通文件桶
     */
    @Value("${minio.bucket.files}")
    private String bucketFiles;

    /**
     * 视频文件桶
     */
    @Value("${minio.bucket.videofiles}")
    private String bucketVideos;

    public MediaFilesServiceImpl(MinioClient minioClient, MediaFilesMapper mediaFilesMapper) {
        this.minioClient = minioClient;
        this.mediaFilesMapper = mediaFilesMapper;
    }

    @Override
    public UploadFileResultDTO uploadFile(Long companyId, UploadFileParamsDTO fileParamsDTO, String localFilePath) {
        //@Transactional添加在该方法时，如果上传文件过程时间较长那么数据库的事务持续时间就会变长，这样数据库连接释放就慢，最终导致数据库连接不够用。

        File file = new File(localFilePath);
        if (!file.exists()) {
            CustomException.cast("文件不存在");
        }
        //文件名称
        String filename = fileParamsDTO.getFilename();
        //文件扩展名
        String extension = filename.substring(filename.lastIndexOf("."));
        //文件 mimetype
        String mimeType = getMimeType(extension);
        //文件Md5值
        String fileMd5 = getFileMd5(file);
        //文件的默认目录
        String defaultFolderPath = getDefaultFolderPath();
        //存储到minio中的对象名（带目录）
        String objectName = defaultFolderPath + fileMd5 + extension;
        //文件大小
        fileParamsDTO.setFileSize(file.length());
        //将文件上传到minio
        addMediaFilesToMinio(localFilePath, mimeType, bucketFiles, objectName);
        //将文件信息存储到数据库中  通过代理对象去调用该方法就可以解决事务问题
        MediaFiles mediaFiles = currentProxy.addMediaFileToDb(companyId, fileMd5, fileParamsDTO, bucketFiles, objectName);
        //返回结果
        return PoDtoConvertMapper.INSTANCE.mediaFiles2UploadFileResultDTO(mediaFiles);
    }

    @Transactional
    @Override
    public MediaFiles addMediaFileToDb(Long companyId, String fileMd5, UploadFileParamsDTO fileParamsDTO,
                                       String bucket, String objectName) {
        //从数据库查询文件
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
        if (!Objects.isNull(mediaFiles)) {
            return mediaFiles;
        }
        //填写信息
        mediaFiles = PoDtoConvertMapper.INSTANCE.uploadFileParamsDto2MediaFiles(fileParamsDTO);
        mediaFiles.setFileId(fileMd5);
        mediaFiles.setId(fileMd5);
        mediaFiles.setCompanyId(companyId);
        mediaFiles.setUrl("/" + bucket + "/" + objectName);
        mediaFiles.setBucket(bucket);
        mediaFiles.setFilePath(objectName);
        mediaFiles.setCreateDate(LocalDateTime.now());
        mediaFiles.setAuditStatus("002003");
        mediaFiles.setStatus("1");
        //保存文件信息到文件表
        int insert = mediaFilesMapper.insert(mediaFiles);
        if (insert < 0) {
            log.error("保存文件信息到数据库失败，{}", mediaFiles);
            CustomException.cast("保存文件信息失败");
        }
        return mediaFiles;
    }

    @Override
    public PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParams pageParams, QueryMediaParamsDTO queryMediaParamsDTO) {
        LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(queryMediaParamsDTO.getFilename()), MediaFiles::getFilename, queryMediaParamsDTO.getFilename())
                .eq(StringUtils.isNotBlank(queryMediaParamsDTO.getFileType()), MediaFiles::getFileType, queryMediaParamsDTO.getFileType())
                .eq(StringUtils.isNotBlank(queryMediaParamsDTO.getAuditStatus()), MediaFiles::getAuditStatus, queryMediaParamsDTO.getAuditStatus());

        Page<MediaFiles> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        Page<MediaFiles> pageResult = mediaFilesMapper.selectPage(page, queryWrapper);
        return new PageResult<>(pageResult.getTotal(), pageParams.getPageNo(), pageParams.getPageSize(), pageResult.getRecords());
    }

    /**
     * 获取文件默认存储目录路径 年/月/日
     *
     * @return 日期字符串
     */
    private String getDefaultFolderPath() {
        String folder = DateUtil.toDateTime(LocalDateTime.now(), DateUtil.YYYY_MM_DD_FORMAT);
        return folder.replace("-", "/") + "/";
    }

    /**
     * 获取文件的Md5
     *
     * @param file 文件
     * @return Md5值
     */
    private String getFileMd5(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            return DigestUtils.md5Hex(fis);
        } catch (Exception e) {
            log.error("获取文件md5失败", e);
            return null;
        }
    }

    /**
     * 获取 MimeType 类型
     *
     * @param extension 后缀名
     * @return 类型
     */
    private String getMimeType(String extension) {
        if (StringUtils.isBlank(extension)) {
            extension = "";
        }
        //根据扩展名取出 mimeType
        ContentInfo contentInfo = ContentInfoUtil.findMimeTypeMatch(extension);
        String mimetype = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        return contentInfo != null ? contentInfo.getMimeType() : mimetype;
    }

    /**
     * 将文件写入minio
     *
     * @param localFilePath 文件地址
     * @param mimetype      媒体类型
     * @param bucket        桶
     * @param objectName    对象名称
     * @return 是否成功
     */
    public boolean addMediaFilesToMinio(String localFilePath, String mimetype, String bucket, String objectName) {
        try {
            UploadObjectArgs objectArgs = UploadObjectArgs.builder()
                    .bucket(bucket)
                    .filename(localFilePath)
                    .object(objectName)
                    .contentType(mimetype)
                    .build();

            minioClient.uploadObject(objectArgs);
            log.info("上传文件到 Minio 成功，bucket：{}，objectName：{}", bucket, objectName);
            return true;
        } catch (Exception e) {
            log.error("上传文件到 Minio 出错，bucket：{}，objectName：{}，错误原因：{}", bucket, objectName, e.getMessage(), e);
            CustomException.cast("上传文件到文件系统失败");
        } finally {
            try {
                //把临时文件删除
                Files.deleteIfExists(Paths.get(localFilePath));
            } catch (IOException e) {
                log.warn("删除临时文件失败，{}", e.getMessage(), e);
            }
        }
        return false;
    }
}
