package com.mewp.edu.content.api;

import com.mewp.edu.content.model.dto.TeachPlanDTO;
import com.mewp.edu.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程计划 前端控制器
 * </p>
 *
 * @author mewp
 * @version 1.0
 * @date 2023/8/27 17:41
 */
@Slf4j
@Api(value = "TeachPlanController", tags = "课程计划管理")
@RestController
@RequestMapping("/teachplan")
public class TeachPlanController {

    @Resource
    private TeachplanService teachplanService;

    @ApiOperation("查询课程计划树形结构")
    @ApiImplicitParam(name = "courseId", value = "课程ID", paramType = "path", dataType = "long", required = true)
    @GetMapping("/{courseId}/tree-nodes")
    public List<TeachPlanDTO> getTreeNodes(@PathVariable("courseId") Long courseId) {
        return teachplanService.findTeachPlanTree(courseId);
    }
}