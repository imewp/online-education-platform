package com.mewp.edu.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mewp.edu.common.exception.CustomException;
import com.mewp.edu.content.mapper.TeachplanMapper;
import com.mewp.edu.content.mapper.TeachplanMediaMapper;
import com.mewp.edu.content.model.dto.BindTeachPlanMediaDTO;
import com.mewp.edu.content.model.po.Teachplan;
import com.mewp.edu.content.model.po.TeachplanMedia;
import com.mewp.edu.content.service.TeachplanMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 课程计划与媒资关系 服务实现类
 * </p>
 *
 * @author mewp
 */
@Slf4j
@Service
public class TeachplanMediaServiceImpl extends ServiceImpl<TeachplanMediaMapper, TeachplanMedia> implements TeachplanMediaService {
    @Resource
    private TeachplanMapper teachplanMapper;
    @Resource
    private TeachplanMediaMapper teachplanMediaMapper;

    @Transactional
    @Override
    public TeachplanMedia associationMedia(BindTeachPlanMediaDTO bindTeachPlanMediaDto) {
        // 教学计划id
        Long teachplanId = bindTeachPlanMediaDto.getTeachplanId();
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if (Objects.isNull(teachplan)) {
            CustomException.cast("教学计划不存在");
        }
        // 获取教学计划的层级，只有第二层级允许绑定媒资信息（第二层级为小节，第一层级为章节）
        Integer grade = teachplan.getGrade();
        if (grade != 2) {
            CustomException.cast("只允许第二级教学计划绑定媒资文件");
        }

        // 课程id
        Long courseId = teachplan.getCourseId();
        // 先删除原来该教学计划绑定的媒资
        teachplanMediaMapper.delete(new LambdaQueryWrapper<TeachplanMedia>()
                .eq(TeachplanMedia::getTeachplanId, teachplanId));
        // 再添加教学计划与媒资的绑定关系
        TeachplanMedia teachplanMedia = new TeachplanMedia();
        teachplanMedia.setCourseId(courseId);
        teachplanMedia.setTeachplanId(teachplanId);
        teachplanMedia.setMediaId(bindTeachPlanMediaDto.getMediaId());
        teachplanMedia.setMediaFilename(bindTeachPlanMediaDto.getFileName());
        teachplanMedia.setCreateDate(LocalDateTime.now());
        teachplanMediaMapper.insert(teachplanMedia);
        return teachplanMedia;
    }

    @Override
    public void deleteAssociationMedia(Long teachPlanId, String mediaId) {
        LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<TeachplanMedia>()
                .eq(TeachplanMedia::getTeachplanId, teachPlanId)
                .eq(TeachplanMedia::getMediaId, mediaId);
        teachplanMediaMapper.delete(queryWrapper);
    }
}
