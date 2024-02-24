package com.mewp.edu.messagesdk.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mewp.edu.messagesdk.mapper.MqMessageHistoryMapper;
import com.mewp.edu.messagesdk.mapper.MqMessageMapper;
import com.mewp.edu.messagesdk.model.po.MqMessage;
import com.mewp.edu.messagesdk.model.po.MqMessageHistory;
import com.mewp.edu.messagesdk.service.MqMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mewp
 */
@Slf4j
@Service
public class MqMessageServiceImpl extends ServiceImpl<MqMessageMapper, MqMessage> implements MqMessageService {
    private final MqMessageMapper mqMessageMapper;
    private final MqMessageHistoryMapper mqMessageHistoryMapper;

    public MqMessageServiceImpl(MqMessageMapper mqMessageMapper, MqMessageHistoryMapper mqMessageHistoryMapper) {
        this.mqMessageMapper = mqMessageMapper;
        this.mqMessageHistoryMapper = mqMessageHistoryMapper;
    }

    @Override
    public List<MqMessage> getMessageList(int shardIndex, int sharedTotal, String messagesType, int count) {
        return mqMessageMapper.selectListByShardIndex(shardIndex, sharedTotal, messagesType, count);
    }

    @Override
    public MqMessage addMessage(String messageType, String businessKey1, String businessKey2, String businessKey3) {
        MqMessage mqMessage = new MqMessage();
        mqMessage.setMessageType(messageType);
        mqMessage.setBusinessKey1(businessKey1);
        mqMessage.setBusinessKey2(businessKey2);
        mqMessage.setBusinessKey3(businessKey3);
        int insert = mqMessageMapper.insert(mqMessage);
        return insert > 0 ? mqMessage : null;
    }

    @Transactional
    @Override
    public int completed(long id) {
        MqMessage mqMessage = new MqMessage();
        // 完成任务
        mqMessage.setState("1");
        int update = mqMessageMapper.update(mqMessage, new LambdaUpdateWrapper<MqMessage>().eq(MqMessage::getId, id));
        if (update <= 0) {
            return 0;
        }
        mqMessage = mqMessageMapper.selectById(id);
        // 添加到历史表
        MqMessageHistory mqMessageHistory = new MqMessageHistory();
        BeanUtils.copyProperties(mqMessage, mqMessageHistory);
        mqMessageHistoryMapper.insert(mqMessageHistory);
        // 删除消息表
        mqMessageMapper.deleteById(id);
        return 1;
    }

    @Override
    public int completedStageOne(long id) {
        MqMessage mqMessage = new MqMessage();
        // 完成第一阶段任务
        mqMessage.setStageState1("1");
        return mqMessageMapper.update(mqMessage, new LambdaUpdateWrapper<MqMessage>().eq(MqMessage::getId, id));
    }

    @Override
    public int completedStageTwo(long id) {
        MqMessage mqMessage = new MqMessage();
        // 完成第二阶段任务
        mqMessage.setStageState2("1");
        return mqMessageMapper.update(mqMessage, new LambdaUpdateWrapper<MqMessage>().eq(MqMessage::getId, id));
    }

    @Override
    public int completedStageThree(long id) {
        MqMessage mqMessage = new MqMessage();
        // 完成第三阶段任务
        mqMessage.setStageState3("1");
        return mqMessageMapper.update(mqMessage, new LambdaUpdateWrapper<MqMessage>().eq(MqMessage::getId, id));
    }

    @Override
    public int completedStageFour(long id) {
        MqMessage mqMessage = new MqMessage();
        // 完成第四阶段任务
        mqMessage.setStageState4("1");
        return mqMessageMapper.update(mqMessage, new LambdaUpdateWrapper<MqMessage>().eq(MqMessage::getId, id));
    }

    @Override
    public int getStageOne(long id) {
        return Integer.parseInt(mqMessageMapper.selectById(id).getStageState1());
    }

    @Override
    public int getStageTwo(long id) {
        return Integer.parseInt(mqMessageMapper.selectById(id).getStageState2());
    }

    @Override
    public int getStageThree(long id) {
        return Integer.parseInt(mqMessageMapper.selectById(id).getStageState3());
    }

    @Override
    public int getStageFour(long id) {
        return Integer.parseInt(mqMessageMapper.selectById(id).getStageState4());
    }
}
