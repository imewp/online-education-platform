package com.mewp.edu.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.mewp.edu.common.exception.CustomException;
import com.mewp.edu.common.model.PageResult;
import com.mewp.edu.common.model.ResponseResult;
import com.mewp.edu.common.param.PageParams;
import com.mewp.edu.common.utils.DateUtil;
import com.mewp.edu.media.mapper.MediaFilesMapper;
import com.mewp.edu.media.model.converter.PoDtoConvertMapper;
import com.mewp.edu.media.model.dto.QueryMediaParamsDTO;
import com.mewp.edu.media.model.dto.UploadFileParamsDTO;
import com.mewp.edu.media.model.dto.UploadFileResultDTO;
import com.mewp.edu.media.model.po.MediaFiles;
import com.mewp.edu.media.service.MediaFilesService;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Resource
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
        boolean b = addMediaFilesToMinio(localFilePath, mimeType, bucketFiles, objectName);
        if (!b) {
            CustomException.cast("上传文件失败");
        }
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
    public PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParams pageParams,
                                                  QueryMediaParamsDTO queryMediaParamsDTO) {
        LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(queryMediaParamsDTO.getFilename()), MediaFiles::getFilename, queryMediaParamsDTO.getFilename())
                .eq(StringUtils.isNotBlank(queryMediaParamsDTO.getFileType()), MediaFiles::getFileType, queryMediaParamsDTO.getFileType())
                .eq(StringUtils.isNotBlank(queryMediaParamsDTO.getAuditStatus()), MediaFiles::getAuditStatus, queryMediaParamsDTO.getAuditStatus())
                .orderByDesc(MediaFiles::getCreateDate);

        Page<MediaFiles> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        Page<MediaFiles> pageResult = mediaFilesMapper.selectPage(page, queryWrapper);
        return new PageResult<>(pageResult.getTotal(), pageParams.getPageNo(), pageParams.getPageSize(), pageResult.getRecords());
    }

    @Override
    public ResponseResult<Boolean> checkFile(String fileMd5) {
        // 查询文件信息
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
        if (!Objects.isNull(mediaFiles)) {
            // 桶
            String bucket = mediaFiles.getBucket();
            // 存储路径
            String filePath = mediaFiles.getFilePath();
            try {
                GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                        .bucket(bucket)         //桶名
                        .object(filePath)      //对象名
                        .build();
                // 文件流
                InputStream stream = minioClient.getObject(getObjectArgs);
                if (Objects.nonNull(stream)) {
                    return ResponseResult.success(true);
                }
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        }
        // 文件不存在
        return ResponseResult.fail();
    }

    @Override
    public ResponseResult<Boolean> checkChunk(String fileMd5, int chunkIndex) {
        // 得到分块文件目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        // 得到分块文件的路径
        Path path = Paths.get(chunkFileFolderPath, String.valueOf(chunkIndex));
        InputStream fis;
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(bucketVideos)         //桶名
                    .object(path.toString())      //对象名
                    .build();
            fis = minioClient.getObject(getObjectArgs);
            if (Objects.nonNull(fis)) {
                // 分块已存在
                return ResponseResult.success(true);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        // 分块未存在
        return ResponseResult.success(false);
    }

    @Override
    public ResponseResult<Boolean> uploadChunk(String fileMd5, int chunkIndex, String localChunkFilePath) {
        // 得到分块文件目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        // 得到分块文件的路径
        Path path = Paths.get(chunkFileFolderPath, String.valueOf(chunkIndex));
        // 文件类型
        String mimeType = getMimeType(null);
        boolean b = addMediaFilesToMinio(localChunkFilePath, mimeType, bucketVideos, path.toString());
        if (b) {
            log.info("上传分块文件成功：{}", path);
            return ResponseResult.success();
        } else {
            log.info("上传分块{} 文件失败：{}", chunkIndex, path);
            return ResponseResult.fail("分片" + chunkIndex + "上传失败");
        }
    }

    @Override
    public ResponseResult<Boolean> mergeChunks(Long companyId, String fileMd5, int chunkTotal,
                                               UploadFileParamsDTO uploadFileParamsDto) {
        // 获取分块文件路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        //将分块文件路径组成 List<ComposeSource>
        List<ComposeSource> sourceObjectList = Stream.iterate(0, i -> ++i)
                .limit(chunkTotal)
                .map(i -> ComposeSource.builder()
                        .bucket(bucketVideos)
                        .object(Paths.get(chunkFileFolderPath, i.toString()).toString())
                        .build())
                .collect(Collectors.toList());

        //=================================合并文件=================================
        //文件名称
        String filename = uploadFileParamsDto.getFilename();
        //文件扩展名
        String extName = filename.substring(filename.lastIndexOf("."));
        //合并后文件路径
        String mergeFilePath = getFilePathByMd5(fileMd5, extName);
        try {
            //合并文件
            ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder()
                    .bucket(bucketVideos)
                    .object(mergeFilePath)
                    .sources(sourceObjectList)
                    .build();
            minioClient.composeObject(composeObjectArgs);
            log.info("合并文件成功：{}", mergeFilePath);
        } catch (Exception e) {
            log.error("合并文件失败，fileMd5 = {}, 异常信息：{}", fileMd5, e.getMessage(), e);
            return ResponseResult.fail("合并文件失败");
        }

        //=================================验证Md5=================================
        //下载合并后的文件
        File minioFile = downLoadFileFromMinIo(bucketVideos, mergeFilePath);
        if (Objects.isNull(minioFile)) {
            log.error("下载合并后的文件失败，mergeFilePath = {}", mergeFilePath);
            return ResponseResult.fail("下载合并后的文件失败");
        }
        String md5Hex = null;
        try (InputStream inputStream = Files.newInputStream(minioFile.toPath())) {
            // minio文件的md5
            md5Hex = DigestUtils.md5Hex(inputStream);
            //进行文件校验，比较MD5，不一致则说明文件不完整
            if (!fileMd5.equals(md5Hex)) {
                return ResponseResult.fail("文件合并校验失败");
            }
            //文件大小
            uploadFileParamsDto.setFileSize(minioFile.length());
        } catch (Exception e) {
            log.error("校验文件失败，MinIO文件Md5 = {}, fileMd5 = {}, 异常原因：{}", md5Hex, fileMd5, e.getMessage(), e);
            return ResponseResult.fail("文件合并校验失败");
        } finally {
            minioFile.delete();
        }

        //===============================文件信息入库===============================
        currentProxy.addMediaFileToDb(companyId, fileMd5, uploadFileParamsDto, bucketVideos, mergeFilePath);

        //===============================清理分块文件===============================
        clearChunkFiles(chunkFileFolderPath, chunkTotal);
        return ResponseResult.success(true);
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

    /**
     * 获得分块文件的目录
     *
     * @param fileMd5 文件md5
     * @return 目录
     */
    private String getChunkFileFolderPath(String fileMd5) {
        Path path = Paths.get(fileMd5.substring(0, 1), fileMd5.substring(1, 2), fileMd5, "chunk");
        return path.toString();
    }

    /**
     * 获得合并后的文件路径
     *
     * @param fileMd5 文件md5，即文件id
     * @param extName 文件扩展名
     * @return 文件路径
     */
    private String getFilePathByMd5(String fileMd5, String extName) {
        String fileName = fileMd5 + extName;
        Path path = Paths.get(fileMd5.substring(0, 1), fileMd5.substring(1, 2), fileMd5, fileName);
        return path.toString();
    }

    /**
     * 从minio下载文件
     *
     * @param bucket   桶
     * @param filePath 文件路径
     * @return 下载后的文件
     */
    private File downLoadFileFromMinIo(String bucket, String filePath) {
        File minioFile;
        FileOutputStream fos = null;
        try {
            GetObjectArgs objectArgs = GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(filePath)
                    .build();
            InputStream stream = minioClient.getObject(objectArgs);
            // 创建临时文件
            minioFile = File.createTempFile("minio", ".merge");
            fos = new FileOutputStream(minioFile);
            IOUtils.copy(stream, fos);
            return minioFile;
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.warn(e.getMessage());
                }
            }
        }
        return null;
    }

    /**
     * 清除分块文件
     *
     * @param chunkFileFolderPath 分块文件路径
     * @param chunkTotal          分块文件总数
     */
    private void clearChunkFiles(String chunkFileFolderPath, int chunkTotal) {
        try {
            List<DeleteObject> deleteObjects = Stream.iterate(0, i -> ++i)
                    .limit(chunkTotal)
                    .map(i -> new DeleteObject(Paths.get(chunkFileFolderPath, String.valueOf(i)).toString()))
                    .collect(Collectors.toList());
            RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder()
                    .bucket(bucketVideos)
                    .objects(deleteObjects).build();
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);
            results.forEach(r -> {
                DeleteError deleteError = null;
                try {
                    deleteError = r.get();
                } catch (Exception e) {
                    log.error("清除分块文件失败，异常原因：{}", e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            log.error("清除分块文件失败，chunkFileFolderPath：{}, 异常原因：{}", chunkFileFolderPath, e.getMessage(), e);
        }
    }
}
