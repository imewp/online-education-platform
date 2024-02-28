package com.mewp.edu.content.jobhandler;

import com.mewp.edu.content.service.CoursePublishService;
import com.mewp.edu.messagesdk.model.po.MqMessage;
import com.mewp.edu.messagesdk.service.MessageProcessAbstract;
import com.mewp.edu.messagesdk.service.MqMessageService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * @author mewp
 * @version 1.0
 * @date 2024/2/24 17:34
 */
@Slf4j
@Component
public class CoursePublishTask extends MessageProcessAbstract {
    private final MqMessageService mqMessageService;
    private final CoursePublishService coursePublishService;

    public CoursePublishTask(MqMessageService mqMessageService, CoursePublishService coursePublishService) {
        super(mqMessageService);
        this.mqMessageService = mqMessageService;
        this.coursePublishService = coursePublishService;
    }

    @XxlJob("CoursePublishJobHandler")
    public void coursePublishJobHandler() {
        // 分片参数
        int shardTotal = XxlJobHelper.getShardTotal();
        int shardIndex = XxlJobHelper.getShardIndex();
        // 执行任务
        process(shardIndex, shardTotal, "course_publish", 30, 60);
    }

    @Override
    public boolean execute(MqMessage mqMessage) {
        // 获取消息相关的业务信息
        long courseId = Long.parseLong(mqMessage.getBusinessKey1());
        // 课程静态化
        generateCourseHtml(mqMessage, courseId);
        // 课程索引
        saveCourseIndex(mqMessage, courseId);
        // 课程缓存
        saveCourseCache(mqMessage, courseId);
        return true;
    }

    /**
     * 生成课程静态化页面并上传到MinIO中
     *
     * @param mqMessage 消息信息
     * @param courseId  课程ID
     */
    private void generateCourseHtml(MqMessage mqMessage, long courseId) {
        log.debug("开始进行课程静态化，课程ID：{}", courseId);
        Long id = mqMessage.getId();

        // 消息幂等性
        int stageOne = mqMessageService.getStageOne(id);
        if (stageOne > 0) {
            log.debug("课程静态化已处理，直接返回，课程ID：{}", courseId);
            return;
        }

        // 生成静态化页面
        File file = coursePublishService.generateCourseHtml(courseId);
        if (file != null) {
            // 上传静态化页面
            coursePublishService.uploadCourseHtml(courseId, file);
        }

        // 保存第一阶段状态
        mqMessageService.completedStageOne(id);
    }

    /**
     * 保存课程索引信息
     *
     * @param mqMessage 消息信息
     * @param courseId  课程ID
     */
    private void saveCourseIndex(MqMessage mqMessage, long courseId) {
        log.debug("开始将课程信息保存到ES中，课程ID：{}", courseId);
        Long id = mqMessage.getId();

        // 消息幂等性
        int stageTwo = mqMessageService.getStageTwo(id);
        if (stageTwo > 0) {
            log.debug("课程信息已写入ES中，无需执行，课程ID：{}", courseId);
            return;
        }

        // todo：手动异常
        int i = 1 / 0;

        // todo：实现ES存储
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 保存第二阶段状态
        mqMessageService.completedStageTwo(id);
    }

    /**
     * 保存课程缓存信息
     *
     * @param mqMessage 消息信息
     * @param courseId  课程ID
     */
    private void saveCourseCache(MqMessage mqMessage, long courseId) {
        log.debug("开始将课程信息缓存到Redis中，课程ID：{}", courseId);
        Long id = mqMessage.getId();

        // 消息幂等性
        int stageThree = mqMessageService.getStageThree(id);
        if (stageThree > 0) {
            log.debug("课程信息已写入Redis中，无需执行，课程ID：{}", courseId);
            return;
        }

        // todo：实现redis缓存
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 保存第三阶段状态
        mqMessageService.completedStageThree(id);
    }

}
