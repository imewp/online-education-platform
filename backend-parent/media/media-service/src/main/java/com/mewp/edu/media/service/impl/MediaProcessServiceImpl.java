package com.mewp.edu.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mewp.edu.media.mapper.MediaFilesMapper;
import com.mewp.edu.media.mapper.MediaProcessHistoryMapper;
import com.mewp.edu.media.mapper.MediaProcessMapper;
import com.mewp.edu.media.model.converter.PoDtoConvertMapper;
import com.mewp.edu.media.model.enums.VideoProcessStatusEnum;
import com.mewp.edu.media.model.po.MediaFiles;
import com.mewp.edu.media.model.po.MediaProcess;
import com.mewp.edu.media.model.po.MediaProcessHistory;
import com.mewp.edu.media.service.MediaProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mewp
 */
@Slf4j
@Service
public class MediaProcessServiceImpl extends ServiceImpl<MediaProcessMapper, MediaProcess> implements MediaProcessService {
    private final MediaFilesMapper mediaFilesMapper;
    private final MediaProcessMapper mediaProcessMapper;
    private final MediaProcessHistoryMapper mediaProcessHistoryMapper;

    public MediaProcessServiceImpl(MediaFilesMapper mediaFilesMapper, MediaProcessMapper mediaProcessMapper,
                                   MediaProcessHistoryMapper mediaProcessHistoryMapper) {
        this.mediaFilesMapper = mediaFilesMapper;
        this.mediaProcessMapper = mediaProcessMapper;
        this.mediaProcessHistoryMapper = mediaProcessHistoryMapper;
    }

    @Override
    public List<MediaProcess> getMeidaProcessList(int shardIndex, int shardTotal, int count) {
        return mediaProcessMapper.selectListByShareIndex(shardIndex, shardTotal, count);
    }

    @Override
    public boolean startTask(long id) {
        return mediaProcessMapper.startTask(id) > 0;
    }

    @Override
    public void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg) {
        // 查询任务，如果不存在则直接返回
        MediaProcess mediaProcess = mediaProcessMapper.selectById(taskId);
        if (Objects.isNull(mediaProcess)) {
            return;
        }
        LambdaUpdateWrapper<MediaProcess> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MediaProcess::getId, taskId);

        // 处理失败，更新任务处理结果
        if ("3".equals(status)) {
            MediaProcess mediaProcessFail = new MediaProcess();
            mediaProcessFail.setStatus(status);
            mediaProcessFail.setErrormsg(errorMsg);
            mediaProcessFail.setFailCount(mediaProcess.getFailCount() + 1);
            mediaProcessMapper.update(mediaProcessFail, updateWrapper);
            log.info("更新任务处理状态为失败，任务信息：{}", mediaProcessFail);
            return;
        }

        //处理成功
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileId);
        Optional.ofNullable(mediaFiles).ifPresent(media -> {
            media.setUrl(url);
            // 更新媒资文件中的访问url
            mediaFilesMapper.updateById(media);
        });
        // 更新任务的url和状态
        mediaProcess.setUrl(url);
        mediaProcess.setStatus(VideoProcessStatusEnum.SUCCESSFULLY_PROCESSED.getCode());
        mediaProcess.setFinishDate(LocalDateTime.now());
        mediaProcessMapper.updateById(mediaProcess);
        // 添加到历史记录表中
        MediaProcessHistory mediaProcessHistory = PoDtoConvertMapper.INSTANCE.mediaProcess2MediaProcessHistory(mediaProcess);
        mediaProcessHistoryMapper.insert(mediaProcessHistory);
        // 删除任务
        mediaProcessMapper.deleteById(taskId);
    }
}
