package com.mewp.edu.content.api;

import com.mewp.edu.common.exception.ValidationGroups;
import com.mewp.edu.common.model.PageResult;
import com.mewp.edu.common.param.PageParams;
import com.mewp.edu.content.model.dto.AddOrUpdateCourseDTO;
import com.mewp.edu.content.model.dto.CourseBaseInfoDTO;
import com.mewp.edu.content.model.dto.QueryCourseParamsDTO;
import com.mewp.edu.content.model.po.CourseBase;
import com.mewp.edu.content.service.CourseBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.groups.Default;

/**
 * <p>
 * 课程基本信息 前端控制器
 * </p>
 *
 * @author mewp
 */
@Slf4j
@Api(value = "课程信息管理", tags = "课程信息管理")
@RestController
@RequestMapping("/course")
public class CourseBaseController {

    @Resource
    private CourseBaseService courseBaseService;

    @ApiOperation("课程分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页码", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", paramType = "query", dataType = "long")
    })
    @PostMapping("/list")
    public PageResult<CourseBase> list(PageParams pageParams,
                                       @RequestBody(required = false) QueryCourseParamsDTO courseParamsDTO) {
        return courseBaseService.queryCourseBasePageList(pageParams, courseParamsDTO);
    }

    @ApiOperation("查询单个课程")
    @ApiImplicitParam(name = "courseId", value = "课程ID", paramType = "path", dataType = "long", required = true)
    @GetMapping("/{courseId}")
    public CourseBaseInfoDTO queryCourseBaseInfo(@PathVariable("courseId") Long courseId) {
        return courseBaseService.queryCourseBaseInfo(courseId);
    }

    @ApiOperation("新增课程基础信息")
    @PostMapping()
    public CourseBaseInfoDTO createCourseBase(@Validated({Default.class, ValidationGroups.Insert.class})
                                              @RequestBody AddOrUpdateCourseDTO courseDTO) {
        //todo：机构ID 暂时使用硬编码
        Long companyId = 1232141425L;
        return courseBaseService.createCourseBase(companyId, courseDTO);
    }

    @ApiOperation("修改课程基础信息")
    @PutMapping()
    public CourseBaseInfoDTO updateCourseBase(@Validated({Default.class, ValidationGroups.Update.class})
                                              @RequestBody AddOrUpdateCourseDTO courseDTO) {
        //todo：机构ID 暂时使用硬编码
        Long companyId = 1232141425L;
        return courseBaseService.updateCourseBase(companyId, courseDTO);
    }

    @ApiOperation("删除单个课程")
    @ApiImplicitParam(name = "courseId", value = "课程ID", paramType = "path", dataType = "long", required = true)
    @DeleteMapping("/{courseId}")
    public void deleteCourseBase(@PathVariable("courseId") Long courseId) {
        //todo：机构ID 暂时使用硬编码
        Long companyId = 1232141425L;
        courseBaseService.deleteCourseBase(companyId, courseId);
    }
}
