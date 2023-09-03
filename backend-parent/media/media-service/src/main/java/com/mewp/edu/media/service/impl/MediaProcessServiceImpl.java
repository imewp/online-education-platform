package com.mewp.edu.media.service.impl;

import com.mewp.edu.media.model.po.MediaProcess;
import com.mewp.edu.media.mapper.MediaProcessMapper;
import com.mewp.edu.media.service.MediaProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mewp
 */
@Slf4j
@Service
public class MediaProcessServiceImpl extends ServiceImpl<MediaProcessMapper, MediaProcess> implements MediaProcessService {

}
