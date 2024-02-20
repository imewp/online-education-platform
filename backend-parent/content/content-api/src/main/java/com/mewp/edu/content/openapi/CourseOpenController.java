package com.mewp.edu.content.openapi;

import com.mewp.edu.content.model.dto.CoursePreviewDTO;
import com.mewp.edu.content.service.CoursePublishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mewp
 * @version 1.0
 * @date 2024/2/20 18:40
 */
@Slf4j
@Api(value = "CourseOpenController", tags = "课程公开查询接口")
@RestController
@RequestMapping("/open/course")
public class CourseOpenController {
    private final CoursePublishService coursePublishService;

    public CourseOpenController(CoursePublishService coursePublishService) {
        this.coursePublishService = coursePublishService;
    }

    @ApiOperation("获取课程预览信息")
    @ApiImplicitParam(name = "courseId", value = "课程ID", paramType = "path", dataType = "long", required = true)
    @GetMapping("/whole/{courseId}")
    public CoursePreviewDTO getPreviewInfo(@PathVariable("courseId") Long courseId) {
        return coursePublishService.getCoursePreviewInfo(courseId);
    }
}
