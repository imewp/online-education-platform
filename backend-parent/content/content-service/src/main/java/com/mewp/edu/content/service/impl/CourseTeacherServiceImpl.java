package com.mewp.edu.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mewp.edu.common.exception.CustomException;
import com.mewp.edu.content.mapper.CourseBaseMapper;
import com.mewp.edu.content.mapper.CourseTeacherMapper;
import com.mewp.edu.content.model.converter.PoDtoConvertMapper;
import com.mewp.edu.content.model.dto.AddOrUpdateCourseTeacherDTO;
import com.mewp.edu.content.model.po.CourseBase;
import com.mewp.edu.content.model.po.CourseTeacher;
import com.mewp.edu.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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

    @Resource
    private CourseBaseMapper courseBaseMapper;

    @Override
    public List<CourseTeacher> listCourseTeacher(Long courseId) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId, courseId);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public CourseTeacher saveOrUpdateTeacher(AddOrUpdateCourseTeacherDTO teacherDTO) {
        Long courseId = teacherDTO.getCourseId();
        //判断下课程是否存在
        findCourseBase(courseId);
        Long id = teacherDTO.getId();
        CourseTeacher courseTeacher = PoDtoConvertMapper.INSTANCE.courseTeacherDto2Vo(teacherDTO);
        //新增操作
        if (Objects.isNull(id)) {
            int insert = baseMapper.insert(courseTeacher);
            if (insert < 1) {
                CustomException.cast("新增教师信息失败");
            }
        } else {
            //修改操作
            int i = baseMapper.updateById(courseTeacher);
            if (i < 1) {
                CustomException.cast("修改教师信息失败");
            }
        }
        return findTeacherById(courseTeacher.getId());
    }


    @Override
    public void deleteTeacher(Long courseId, Long teacherId) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId, courseId)
                .eq(CourseTeacher::getId, teacherId);
        int i = baseMapper.delete(queryWrapper);
        if (i < 1) {
            CustomException.cast("删除教师信息失败");
        }
    }

    @Override
    public CourseTeacher findTeacherById(Long teacherId) {
        return baseMapper.selectById(teacherId);
    }

    /**
     * 根据课程ID查询数据，查询不到则抛出异常
     *
     * @param courseId 课程ID
     * @return 课程信息
     */
    private CourseBase findCourseBase(Long courseId) {
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (Objects.isNull(courseBase)) {
            CustomException.cast("课程信息不存在");
        }
        return courseBase;
    }
}
