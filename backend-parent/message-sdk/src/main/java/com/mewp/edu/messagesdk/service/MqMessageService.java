package com.mewp.edu.messagesdk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mewp.edu.messagesdk.model.po.MqMessage;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mewp
 * @since 2024-02-24
 */
public interface MqMessageService extends IService<MqMessage> {

    /**
     * 扫描消息表记录
     *
     * @param shardIndex   分片序号
     * @param sharedTotal  分片总数
     * @param messagesType 消息类型
     * @param count        扫描记录数
     * @return 消息记录
     */
    List<MqMessage> getMessageList(int shardIndex, int sharedTotal, String messagesType, int count);

    /**
     * 添加消息
     *
     * @param messageType  小谢类型
     * @param businessKey1 业务ID
     * @param businessKey2 业务ID
     * @param businessKey3 业务ID
     * @return 消息内容
     */
    MqMessage addMessage(String messageType, String businessKey1, String businessKey2, String businessKey3);


    /**
     * 完成任务
     *
     * @param id 消息ID
     * @return 是否完成（0：失败，1：成功）
     */
    int completed(long id);

    /**
     * 完成第一阶段任务
     *
     * @param id 消息ID
     * @return 是否完成（0：失败，1：成功）
     */
    int completedStageOne(long id);

    /**
     * 完成第二阶段任务
     *
     * @param id 消息ID
     * @return 是否完成（0：失败，1：成功）
     */
    int completedStageTwo(long id);

    /**
     * 完成第三阶段任务
     *
     * @param id 消息ID
     * @return 是否完成（0：失败，1：成功）
     */
    int completedStageThree(long id);

    /**
     * 完成第四阶段任务
     *
     * @param id 消息ID
     * @return 是否完成（0：失败，1：成功）
     */
    int completedStageFour(long id);

    /**
     * 查询第一阶段任务状态
     *
     * @param id 消息ID
     * @return 任务状态（0：未完成，1：已完成）
     */
    int getStageOne(long id);

    /**
     * 查询第二阶段任务状态
     *
     * @param id 消息ID
     * @return 任务状态（0：未完成，1：已完成）
     */
    int getStageTwo(long id);

    /**
     * 查询第三阶段任务状态
     *
     * @param id 消息ID
     * @return 任务状态（0：未完成，1：已完成）
     */
    int getStageThree(long id);

    /**
     * 查询第四阶段任务状态
     *
     * @param id 消息ID
     * @return 任务状态（0：未完成，1：已完成）
     */
    int getStageFour(long id);
}
