package com.mewp.edu.content.api;

import com.mewp.edu.content.model.dto.CourseCategoryTreeDTO;
import com.mewp.edu.content.service.CourseCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 课程分类 前端控制器
 *
 * @author mewp
 * @version 1.0
 * @date 2023/8/21 00:05
 */
@Slf4j
@Api(value = "CourseCategoryController", tags = "课程分类管理")
@RestController
@RequestMapping("/course-category")
public class CourseCategoryController {

    @Resource
    private CourseCategoryService courseCategoryService;

    @ApiOperation("获取课程分类的树形结构")
    @GetMapping("/tree-nodes")
    public List<CourseCategoryTreeDTO> queryTreeNodes() {
        return courseCategoryService.queryTreeNodes("1");
    }

}
