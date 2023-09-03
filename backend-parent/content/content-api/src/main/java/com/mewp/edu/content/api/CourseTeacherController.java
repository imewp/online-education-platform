package com.mewp.edu.content.api;

import com.mewp.edu.content.model.dto.AddOrUpdateCourseTeacherDTO;
import com.mewp.edu.content.model.po.CourseTeacher;
import com.mewp.edu.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mewp
 * @version 1.0
 * @date 2023/8/28 18:46
 */
@Slf4j
@Api(value = "CourseTeacherController", tags = "课程师资管理")
@RestController
@RequestMapping("/courseTeacher")
public class CourseTeacherController {
    @Resource
    private CourseTeacherService courseTeacherService;

    @ApiOperation("查询课程的师资列表")
    @ApiImplicitParam(name = "courseId", value = "课程ID", paramType = "path", dataType = "long", required = true)
    @GetMapping("/list/{courseId}")
    public List<CourseTeacher> listCourseTeacher(@PathVariable("courseId") Long courseId) {
        return courseTeacherService.listCourseTeacher(courseId);
    }

    @ApiOperation("添加或修改教师")
    @PostMapping()
    public CourseTeacher saveOrUpdateTeacher(@Validated @RequestBody AddOrUpdateCourseTeacherDTO teacherDTO) {
        return courseTeacherService.saveOrUpdateTeacher(teacherDTO);
    }

    @ApiOperation("删除教师")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId", value = "课程ID", paramType = "path", dataType = "long", required = true),
            @ApiImplicitParam(name = "id", value = "教师ID", paramType = "path", dataType = "long", required = true)
    })
    @DeleteMapping("/course/{courseId}/{teacherId}")
    public void deleteTeacher(@PathVariable("courseId") Long courseId,
                              @PathVariable("teacherId") Long teacherId) {
        courseTeacherService.deleteTeacher(courseId, teacherId);
    }
}
