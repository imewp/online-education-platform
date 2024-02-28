package com.mewp.edu.search.controller;

import com.mewp.edu.common.exception.CustomException;
import com.mewp.edu.search.entity.po.CourseIndex;
import com.mewp.edu.search.service.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mewp
 * @version 1.0
 * @date 2024/2/28 11:03
 */
@Api(value = "课程信息索引接口", tags = "课程信息索引接口")
@RestController
@RequestMapping("/index")
public class CourseIndexController {

    @Value("${elasticsearch.course.index}")
    private String courseIndexStore;

    private final IndexService indexService;

    public CourseIndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    @ApiOperation("添加课程索引")
    @PostMapping("course")
    public Boolean add(@RequestBody CourseIndex courseIndex) {
        Long id = courseIndex.getId();
        if (id == null) {
            CustomException.cast("课程id为空");
        }
        Boolean result = indexService.addCourseIndex(courseIndexStore, String.valueOf(id), courseIndex);
        if (!result) {
            CustomException.cast("添加课程索引失败");
        }
        return true;
    }
}
