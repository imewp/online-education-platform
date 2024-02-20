package com.mewp.edu.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mewp.edu.content.model.dto.CoursePreviewDTO;
import com.mewp.edu.content.model.po.CoursePublish;

/**
 * <p>
 * 课程发布 服务类
 * </p>
 *
 * @author mewp
 * @since 2023-08-20
 */
public interface CoursePublishService extends IService<CoursePublish> {

    /**
     * 根据课程ID获取课程预览信息
     *
     * @param courseId 课程ID
     * @return 课程预览信息
     */
    public CoursePreviewDTO getCoursePreviewInfo(Long courseId);
}
