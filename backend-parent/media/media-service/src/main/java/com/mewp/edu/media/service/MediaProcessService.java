package com.mewp.edu.media.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mewp.edu.media.model.po.MediaProcess;

import java.util.List;

/**
 * <p>
 * 媒资文件处理业务服务类
 * </p>
 *
 * @author mewp
 * @since 2023-09-03
 */
public interface MediaProcessService extends IService<MediaProcess> {
    /**
     * 获取待处理任务
     *
     * @param shardIndex 分片序号
     * @param shardTotal 分片总数
     * @param count      获取记录数
     * @param failCount  失败次数
     * @return 待处理的媒资文件列表
     */
    List<MediaProcess> getMeidaProcessList(int shardIndex, int shardTotal, int count, int failCount);

    /**
     * 开启一个任务
     *
     * @param id        任务id
     * @param failCount 失败次数
     * @return true 开启成功，false 开启失败
     */
    boolean startTask(long id, int failCount);

    /**
     * 保存任务结果
     *
     * @param taskId   任务id
     * @param status   任务状态
     * @param fileId   文件id
     * @param url      文件访问地址
     * @param errorMsg 错误信息
     */
    void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg);
}
