package com.mewp.edu.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mewp.edu.content.mapper.CoursePublishMapper;
import com.mewp.edu.content.model.dto.CourseBaseInfoDTO;
import com.mewp.edu.content.model.dto.CoursePreviewDTO;
import com.mewp.edu.content.model.dto.TeachPlanDTO;
import com.mewp.edu.content.model.po.CoursePublish;
import com.mewp.edu.content.service.CourseBaseService;
import com.mewp.edu.content.service.CoursePublishService;
import com.mewp.edu.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程发布 服务实现类
 * </p>
 *
 * @author mewp
 */
@Slf4j
@Service
public class CoursePublishServiceImpl extends ServiceImpl<CoursePublishMapper, CoursePublish> implements CoursePublishService {
    private final CourseBaseService courseBaseService;
    private final TeachplanService teachplanService;

    public CoursePublishServiceImpl(CourseBaseService courseBaseService, TeachplanService teachplanService) {
        this.courseBaseService = courseBaseService;
        this.teachplanService = teachplanService;
    }

    @Override
    public CoursePreviewDTO getCoursePreviewInfo(Long courseId) {
        CoursePreviewDTO coursePreviewDto = new CoursePreviewDTO();
        // 根据课程ID查询 课程基本信息、营销信息
        List<TeachPlanDTO> teachPlanTree = teachplanService.findTeachPlanTree(courseId);
        // 根据课程ID，查询课程计划
        CourseBaseInfoDTO courseBaseInfoDto = courseBaseService.queryCourseBaseInfo(courseId);
        // 封装返回
        coursePreviewDto.setCourseBase(courseBaseInfoDto);
        coursePreviewDto.setTeachPlans(teachPlanTree);
        return coursePreviewDto;
    }
}
