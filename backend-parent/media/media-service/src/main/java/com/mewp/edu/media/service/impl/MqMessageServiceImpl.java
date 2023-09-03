package com.mewp.edu.media.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mewp.edu.media.mapper.MqMessageMapper;
import com.mewp.edu.media.model.po.MqMessage;
import com.mewp.edu.media.service.MqMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mewp
 */
@Slf4j
@Service
public class MqMessageServiceImpl extends ServiceImpl<MqMessageMapper, MqMessage> implements MqMessageService {

}
