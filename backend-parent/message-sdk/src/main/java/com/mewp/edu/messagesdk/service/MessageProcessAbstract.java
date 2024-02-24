package com.mewp.edu.messagesdk.service;

import com.mewp.edu.messagesdk.model.po.MqMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 消息处理抽象类
 *
 * @author mewp
 * @version 1.0
 * @date 2024/2/24 16:38
 */
@Slf4j
@Data
public abstract class MessageProcessAbstract {

    private final MqMessageService mqMessageService;

    public MessageProcessAbstract(MqMessageService mqMessageService) {
        this.mqMessageService = mqMessageService;
    }

    /**
     * 任务执行
     *
     * @param mqMessage 执行任务内容
     * @return true:执行成功 false:执行失败
     */
    public abstract boolean execute(MqMessage mqMessage);

    /**
     * 扫描消息表多线程执行任务
     *
     * @param shardIndex  分片序号
     * @param shardTotal  分片总数
     * @param messageType 消息类型
     * @param count       一次取出任务总数
     * @param timeout     预估任务执行时间，到此时间如果任务还没有执行结束则强制结束（单位：秒）
     */
    public void process(int shardIndex, int shardTotal, String messageType, int count, long timeout) {
        try {
            // 获取任务清单
            List<MqMessage> messageList = mqMessageService.getMessageList(shardIndex, shardTotal, messageType, count);
            // 任务数量
            int size = messageList.size();
            log.debug("取出待处理消息 {} 条", size);
            if (size == 0) {
                return;
            }
            // 创建线程池
            ExecutorService threadPool = Executors.newFixedThreadPool(size);
            // 计数器
            CountDownLatch countDownLatch = new CountDownLatch(size);
            messageList.forEach(message -> threadPool.execute(() -> {
                log.debug("开始任务：{}", message);
                try {
                    boolean result = execute(message);
                    if (result) {
                        log.debug("任务执行成功：{}", message);
                        // 更新任务状态，删除消息表记录，添加到历史表
                        int completed = mqMessageService.completed(message.getId());
                        if (completed > 0) {
                            log.debug("任务处理成功：{}", message);
                        } else {
                            log.debug("任务处理失败：{}", message);
                        }
                    }
                } catch (Exception e) {
                    log.error("任务：{}, 出现异常：{}", message, e.getMessage(), e);
                }
                // 计数
                countDownLatch.countDown();
                log.debug("结束任务：{}", message);
            }));

            // 等待，给一个充裕的超时时间，防止无限等待，到达超时时间还没有处理完成则结束任务
            countDownLatch.await(timeout, TimeUnit.SECONDS);
            log.debug("结束...");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
