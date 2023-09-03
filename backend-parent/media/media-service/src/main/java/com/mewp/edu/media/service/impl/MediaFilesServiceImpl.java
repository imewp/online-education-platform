package com.mewp.edu.media.service.impl;

import com.mewp.edu.media.model.po.MediaFiles;
import com.mewp.edu.media.mapper.MediaFilesMapper;
import com.mewp.edu.media.service.MediaFilesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 媒资信息 服务实现类
 * </p>
 *
 * @author mewp
 */
@Slf4j
@Service
public class MediaFilesServiceImpl extends ServiceImpl<MediaFilesMapper, MediaFiles> implements MediaFilesService {

}
