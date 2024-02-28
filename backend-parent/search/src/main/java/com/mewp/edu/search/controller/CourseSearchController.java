package com.mewp.edu.search.controller;

import com.mewp.edu.common.param.PageParams;
import com.mewp.edu.search.entity.dto.SearchCourseParamDTO;
import com.mewp.edu.search.entity.dto.SearchPageResultDTO;
import com.mewp.edu.search.entity.po.CourseIndex;
import com.mewp.edu.search.service.CourseSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mewp
 * @version 1.0
 * @date 2024/2/28 11:04
 */
@Api(value = "课程搜索接口", tags = "课程搜索接口")
@RestController
@RequestMapping("/course")
public class CourseSearchController {
    private final CourseSearchService courseSearchService;

    public CourseSearchController(CourseSearchService courseSearchService) {
        this.courseSearchService = courseSearchService;
    }

    @ApiOperation("课程搜索列表")
    @GetMapping("/list")
    public SearchPageResultDTO<CourseIndex> list(PageParams pageParams, SearchCourseParamDTO searchCourseParamDto) {
        return courseSearchService.queryCoursePubIndex(pageParams, searchCourseParamDto);
    }
}
