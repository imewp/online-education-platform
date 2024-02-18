package com.mewp.edu.media.jobhandler;

import com.mewp.edu.media.model.enums.VideoProcessStatusEnum;
import com.mewp.edu.media.model.po.MediaProcess;
import com.mewp.edu.media.service.MediaFilesService;
import com.mewp.edu.media.service.MediaProcessService;
import com.mewp.edu.media.utils.Mp4VideoUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 视频处理任务
 * fixme：优化日志输出，保证log和xxl-job日志输出一致，能够在服务端输出和xxl-job日志管理中记录
 *
 * @author mewp
 * @version 1.0
 * @date 2023/12/31 15:56
 */
@Slf4j
@Component
public class VideoHandleJob {
    private final MediaFilesService mediaFilesService;
    private final MediaProcessService mediaProcessService;

    public VideoHandleJob(MediaFilesService mediaFilesService, MediaProcessService mediaProcessService) {
        this.mediaFilesService = mediaFilesService;
        this.mediaProcessService = mediaProcessService;
    }

    @XxlJob("videoJobHandler")
    public void videoJobHandler() throws InterruptedException {
        // 分片参数
        int shardTotal = XxlJobHelper.getShardTotal();
        int shardIndex = XxlJobHelper.getShardIndex();

        log.debug("分片参数：当前分片序号 = {}, 总分片数 = {}", shardIndex, shardTotal);
        XxlJobHelper.log("分片参数：当前分片序号 = {}, 总分片数 = {}", shardIndex, shardTotal);

        List<MediaProcess> mediaProcessList;
        int size;
        try {
            // 取出CPU核心数作为一次处理数据的条数
            int processors = Runtime.getRuntime().availableProcessors();
            // 一次处理视频数量不要超过CPU核心数
            mediaProcessList = mediaProcessService.getMeidaProcessList(shardIndex, shardTotal, processors, 3);
            size = mediaProcessList.size();
            log.debug("取出待处理视频任务 {} 条", size);
            XxlJobHelper.log("取出待处理视频任务 {} 条", size);
        } catch (Exception e) {
            log.error("视频处理过程中出现异常：{}", e.getMessage(), e);
            XxlJobHelper.log("视频处理过程中出现异常：{}", e.getMessage());
            return;
        }
        // 启动size个线程的线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(size);
        // 计数器
        CountDownLatch countDownLatch = new CountDownLatch(size);
        // 将处理任务加入线程池
        mediaProcessList.forEach(mediaProcess -> threadPool.execute(() -> {
            try {
                // 任务id
                Long taskId = mediaProcess.getId();
                // 抢占任务
                boolean b = mediaProcessService.startTask(taskId, 3);
                if (!b) {
                    log.debug("抢占任务失败，任务id：{}", taskId);
                    XxlJobHelper.log("抢占任务失败，任务id：{}", taskId);
                    return;
                }
                log.info("开始执行任务，taskId：{}，具体内容为：{}", taskId, mediaProcess);
                XxlJobHelper.log("开始执行任务，taskId：{}，具体内容为：{}", taskId, mediaProcess);
                // 桶
                String bucket = mediaProcess.getBucket();
                // 存储路径
                String filePath = mediaProcess.getFilePath();
                // 原始视频的md5值
                String fileId = mediaProcess.getFileId();
                // 将要处理的文件下载到服务器上
                File originalFile = mediaFilesService.downLoadFileFromMinIo(bucket, filePath);
                if (originalFile == null) {
                    log.info("下载待处理文件失败，originalFile：{}", bucket.concat(filePath));
                    XxlJobHelper.log("下载待处理文件失败，originalFile：{}", bucket.concat(filePath));
                    mediaProcessService.saveProcessFinishStatus(taskId, VideoProcessStatusEnum.PROCESSING_FAILED.getCode(),
                            fileId, null, "下载待处理文件失败");
                    return;
                }
                // 处理下载的视频文件
                File mp4File;
                try {
                    mp4File = File.createTempFile("mp4", ".mp4");
                } catch (IOException e) {
                    log.error("创建MP4临时文件失败，失败原因：{}", e.getMessage(), e);
                    XxlJobHelper.log("创建MP4临时文件失败，失败原因：{}", e.getMessage());
                    mediaProcessService.saveProcessFinishStatus(taskId, VideoProcessStatusEnum.PROCESSING_FAILED.getCode(),
                            fileId, null, "创建MP4临时文件失败");
                    return;
                }
                // 视频处理结果
                String result = "";
                try {
                    // 开始处理视频
                    XxlJobHelper.log("开始处理视频...");
                    //fixme： 临时 ffmpeg 安装路径
                    String ffmpegPath = "ffmpeg";
                    Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpegPath, originalFile.getAbsolutePath(),
                            mp4File.getName(), mp4File.getAbsolutePath());
                    // 开始视频转换，成功将返回 success
                    result = mp4VideoUtil.generateMp4();
                } catch (Exception e) {
                    log.error("处理视频文件：{}, 出错：{}", filePath, e.getMessage(), e);
                    XxlJobHelper.log("处理视频文件：{}, 出错：{}", filePath, e.getMessage());
                }
                if (!"success".equals(result)) {
                    // 记录错误信息
                    log.error("处理视频失败,视频地址:{},错误信息:{}", bucket + " " + filePath, result);
                    XxlJobHelper.log("处理视频失败,视频地址:{},错误信息:{}",
                            bucket + " " + filePath, result);
                    mediaProcessService.saveProcessFinishStatus(taskId, VideoProcessStatusEnum.PROCESSING_FAILED.getCode(),
                            fileId, null, "处理视频失败");
                    return;
                }

                // 将mp4上传至minio
                // mp4在minio的存储路径
                String objectName = mediaFilesService.getFilePathByMd5(fileId, ".mp4");
                // 访问url
                String url = "/" + bucket + "/" + objectName;
                try {
                    mediaFilesService.addMediaFilesToMinio(mp4File.getAbsolutePath(), "video/mp4", bucket, objectName);
                    // 将url存储至数据，并更新状态为成功，并将待处理视频记录删除存入历史
                    mediaProcessService.saveProcessFinishStatus(mediaProcess.getId(),
                            VideoProcessStatusEnum.SUCCESSFULLY_PROCESSED.getCode(), fileId, url, null);
                } catch (Exception e) {
                    log.error("上传视频失败或入库失败,视频地址:{},错误信息:{}", bucket + objectName, e.getMessage(), e);
                    XxlJobHelper.log("上传视频失败或入库失败,视频地址:{},错误信息:{}",
                            bucket + objectName, e.getMessage());
                    //最终还是失败了
                    mediaProcessService.saveProcessFinishStatus(mediaProcess.getId(),
                            VideoProcessStatusEnum.PROCESSING_FAILED.getCode(), fileId, null, "处理后视频上传或入库失败");
                }
            } finally {
                countDownLatch.countDown();
            }
        }));

        // 等待,给一个充裕的超时时间,防止无限等待，到达超时时间还没有处理完成则结束任务
        countDownLatch.await(30, TimeUnit.MINUTES);
    }
}

