package com.mewp.edu.media.service;

import com.mewp.edu.common.model.PageResult;
import com.mewp.edu.common.model.ResponseResult;
import com.mewp.edu.common.param.PageParams;
import com.mewp.edu.media.model.dto.QueryMediaParamsDTO;
import com.mewp.edu.media.model.dto.UploadFileParamsDTO;
import com.mewp.edu.media.model.dto.UploadFileResultDTO;
import com.mewp.edu.media.model.po.MediaFiles;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 媒资信息 服务类
 * </p>
 *
 * @author mewp
 * @since 2023-09-03
 */
public interface MediaFilesService extends IService<MediaFiles> {

    /**
     * 上传文件
     *
     * @param companyId     公司ID
     * @param fileParamsDTO 上传文件信息
     * @param localFilePath 文件磁盘路径
     * @return 文件信息
     */
    UploadFileResultDTO uploadFile(Long companyId, UploadFileParamsDTO fileParamsDTO, String localFilePath);

    /**
     * 将文件信息添加到文件表
     *
     * @param companyId     机构ID
     * @param fileMd5       文件Md5值
     * @param fileParamsDTO 上传文件的信息
     * @param bucket        桶
     * @param objectName    对象名称
     * @return 文件信息
     */
    MediaFiles addMediaFileToDb(Long companyId, String fileMd5, UploadFileParamsDTO fileParamsDTO,
                                String bucket, String objectName);

    /**
     * 媒资文件查询
     *
     * @param companyId           公司ID
     * @param pageParams          分页参数
     * @param queryMediaParamsDTO 查询参数
     * @return 文件列表
     */
    PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParams pageParams,
                                           QueryMediaParamsDTO queryMediaParamsDTO);

    /**
     * 检查文件是否存在
     *
     * @param fileMd5 文件md5
     * @return false：不存在，true：存在
     */
    ResponseResult<Boolean> checkFile(String fileMd5);

    /**
     * 检查分块是否存在
     *
     * @param fileMd5    文件md5
     * @param chunkIndex 分块序号
     * @return false：不存在，true：存在
     */
    ResponseResult<Boolean> checkChunk(String fileMd5, int chunkIndex);

    /**
     * 上传分块文件
     *
     * @param fileMd5            文件md5
     * @param chunkIndex         分块序号
     * @param localChunkFilePath 分块文件本地路径
     * @return 响应结果
     */
    ResponseResult<Boolean> uploadChunk(String fileMd5, int chunkIndex, String localChunkFilePath);

    /**
     * 合并分块文件
     *
     * @param companyId           机构ID
     * @param fileMd5             文件md5
     * @param chunkTotal          分块总数
     * @param uploadFileParamsDto 上传文件信息
     * @return 响应结果
     */
    ResponseResult<Boolean> mergeChunks(Long companyId, String fileMd5, int chunkTotal,
                                        UploadFileParamsDTO uploadFileParamsDto);
}
