package com.mewp.edu.messagesdk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mewp.edu.messagesdk.model.po.MqMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author mewp
 */
public interface MqMessageMapper extends BaseMapper<MqMessage> {

    /**
     * 分片查询
     *
     * @param shardIndex   分片索引
     * @param sharedTotal  分片总数
     * @param messagesType 消息类型
     * @param count        查询条数
     * @return 消息列表
     */
    List<MqMessage> selectListByShardIndex(@Param("shardIndex") int shardIndex,
                                           @Param("sharedTotal") int sharedTotal,
                                           @Param("messagesType") String messagesType,
                                           @Param("count") int count);
}
