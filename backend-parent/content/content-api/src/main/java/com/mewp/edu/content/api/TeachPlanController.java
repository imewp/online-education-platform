package com.mewp.edu.content.api;

import com.mewp.edu.content.model.dto.AddOrUpdateTeachPlanDTO;
import com.mewp.edu.content.model.dto.BindTeachPlanMediaDTO;
import com.mewp.edu.content.model.dto.TeachPlanDTO;
import com.mewp.edu.content.service.TeachplanMediaService;
import com.mewp.edu.content.service.TeachplanService;
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

    @Resource
    private TeachplanMediaService teachplanMediaService;

    @ApiOperation("查询课程计划树形结构")
    @ApiImplicitParam(name = "courseId", value = "课程ID", paramType = "path", dataType = "long", required = true)
    @GetMapping("/{courseId}/tree-nodes")
    public List<TeachPlanDTO> getTreeNodes(@PathVariable("courseId") Long courseId) {
        return teachplanService.findTeachPlanTree(courseId);
    }

    @ApiOperation("添加或修改课程计划")
    @PostMapping()
    public void saveOrUpdatePlan(@RequestBody @Validated AddOrUpdateTeachPlanDTO teacherPlanDTO) {
        teachplanService.saveOrUpdatePlan(teacherPlanDTO);
    }

    @ApiOperation("删除课程计划")
    @ApiImplicitParam(name = "id", value = "课程计划ID", paramType = "path", dataType = "long", required = true)
    @DeleteMapping("/{id}")
    public void deleteTeachPlan(@PathVariable("id") Long id) {
        teachplanService.deleteTeachPlan(id);
    }

    @ApiOperation("课程计划排序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "moveType", value = "移动类型（movedown向下，moveup向上）", paramType = "path",
                    dataType = "string", required = true),
            @ApiImplicitParam(name = "id", value = "课程计划ID", paramType = "path", dataType = "long", required = true)
    })
    @PostMapping("/{moveType}/{id}")
    public void moveTeachPlan(@PathVariable("moveType") String moveType,
                              @PathVariable("id") Long id) {
        teachplanService.moveTeachPlan(moveType, id);
    }


    @ApiOperation("课程计划和媒资信息绑定")
    @PostMapping("/association/media")
    public void associationMedia(@RequestBody @Validated BindTeachPlanMediaDTO bindTeachPlanMediaDto) {
        teachplanMediaService.associationMedia(bindTeachPlanMediaDto);
    }

    @ApiOperation("课程计划解除视频绑定")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "teachPlanId", value = "课程计划ID", paramType = "path",
                    dataType = "long", required = true),
            @ApiImplicitParam(name = "mediaId", value = "媒资文件id", paramType = "path",
                    dataType = "string", required = true)
    })
    @DeleteMapping("/association/media/{teachPlanId}/{mediaId}")
    public void deleteAssociationMedia(@PathVariable("teachPlanId") Long teachPlanId,
                                       @PathVariable("mediaId") String mediaId) {
        teachplanMediaService.deleteAssociationMedia(teachPlanId, mediaId);
    }
}
