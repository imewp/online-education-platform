package com.mewp.edu.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mewp.edu.content.model.dto.AddOrUpdateCourseTeacherDTO;
import com.mewp.edu.content.model.po.CourseTeacher;

import java.util.List;

/**
 * <p>
 * 课程-教师关系表 服务类
 * </p>
 *
 * @author mewp
 * @since 2023-08-20
 */
public interface CourseTeacherService extends IService<CourseTeacher> {

    /**
     * 查询当前课程的教师列表
     *
     * @param courseId 课程ID
     * @return 教师列表
     */
    List<CourseTeacher> listCourseTeacher(Long courseId);

    /**
     * 添加或修改教师信息
     *
     * @param teacherDTO 教师信息
     * @return 教师信息
     */
    CourseTeacher saveOrUpdateTeacher(AddOrUpdateCourseTeacherDTO teacherDTO);

    /**
     * 删除教师
     *
     * @param courseId  课程ID
     * @param teacherId 教师ID
     */
    void deleteTeacher(Long courseId, Long teacherId);

    /**
     * 根据ID查询教师信息
     *
     * @param teacherId 教师ID
     * @return 教师信息
     */
    CourseTeacher findTeacherById(Long teacherId);
}
