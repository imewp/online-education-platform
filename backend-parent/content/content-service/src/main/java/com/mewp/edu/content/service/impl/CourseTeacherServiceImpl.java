package com.mewp.edu.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mewp.edu.content.mapper.CourseTeacherMapper;
import com.mewp.edu.content.model.po.CourseTeacher;
import com.mewp.edu.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程-教师关系表 服务实现类
 * </p>
 *
 * @author mewp
 */
@Slf4j
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements CourseTeacherService {

}
