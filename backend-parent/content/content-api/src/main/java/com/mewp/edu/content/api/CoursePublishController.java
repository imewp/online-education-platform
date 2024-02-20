package com.mewp.edu.content.api;

import com.mewp.edu.content.model.dto.CoursePreviewDTO;
import com.mewp.edu.content.service.CoursePublishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author mewp
 * @version 1.0
 * @date 2024/2/20 15:44
 */
@Slf4j
@Api(value = "CoursePublishController", tags = "课程发布管理")
@Controller
public class CoursePublishController {
    private final CoursePublishService coursePublishService;

    public CoursePublishController(CoursePublishService coursePublishService) {
        this.coursePublishService = coursePublishService;
    }

    @ApiOperation("课程预览")
    @ApiImplicitParam(name = "courseId", value = "课程ID", paramType = "path", dataType = "long", required = true)
    @GetMapping("/coursepreview/{courseId}")
    public ModelAndView preview(@PathVariable("courseId") Long courseId) {
        ModelAndView modelAndView = new ModelAndView();
        CoursePreviewDTO coursePreviewInfo = coursePublishService.getCoursePreviewInfo(courseId);
        modelAndView.setViewName("course_template");
        modelAndView.addObject("model", coursePreviewInfo);
        return modelAndView;
    }
}
