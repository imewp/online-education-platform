package com.mewp.edu.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mewp.edu.content.model.dto.CoursePreviewDTO;
import com.mewp.edu.content.model.po.CoursePublish;

import java.io.File;

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
    CoursePreviewDTO getCoursePreviewInfo(Long courseId);

    /**
     * 提交课程审核
     *
     * @param companyId 机构ID
     * @param courseId  课程ID
     */
    void commitAudit(Long companyId, Long courseId);

    /**
     * 课程发布
     *
     * @param companyId 机构ID
     * @param courseId  课程ID
     */
    void publish(Long companyId, Long courseId);

    /**
     * 课程静态化
     *
     * @param courseId 课程ID
     * @return 静态化文件
     */
    File generateCourseHtml(Long courseId);

    /**
     * 上传静态化页面
     *
     * @param courseId 课程ID
     * @param file     静态化文件
     */
    void uploadCourseHtml(Long courseId, File file);
}
