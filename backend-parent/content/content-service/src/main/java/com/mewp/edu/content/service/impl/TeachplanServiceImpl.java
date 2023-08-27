package com.mewp.edu.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mewp.edu.content.mapper.TeachplanMapper;
import com.mewp.edu.content.model.dto.TeachPlanDTO;
import com.mewp.edu.content.model.po.Teachplan;
import com.mewp.edu.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程计划 服务实现类
 * </p>
 *
 * @author mewp
 */
@Slf4j
@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan> implements TeachplanService {

    @Override
    public List<TeachPlanDTO> findTeachPlanTree(Long courseId) {
        return baseMapper.selectTreeNodes(courseId);
    }
}
