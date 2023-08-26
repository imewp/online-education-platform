package com.mewp.edu.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mewp.edu.common.model.PageResult;
import com.mewp.edu.common.param.PageParams;
import com.mewp.edu.content.model.dto.AddOrUpdateCourseDTO;
import com.mewp.edu.content.model.dto.CourseBaseInfoDTO;
import com.mewp.edu.content.model.dto.QueryCourseParamsDTO;
import com.mewp.edu.content.model.po.CourseBase;

/**
 * <p>
 * 课程基本信息 服务类
 * </p>
 *
 * @author mewp
 * @since 2023-08-20
 */
public interface CourseBaseService extends IService<CourseBase> {

    /**
     * 课程分页查询
     *
     * @param pageParams      分页参数
     * @param courseParamsDTO 查询参数
     * @return 分页结果
     */
    PageResult<CourseBase> queryCourseBasePageList(PageParams pageParams, QueryCourseParamsDTO courseParamsDTO);

    /**
     * 添加课程基本信息
     *
     * @param companyId 教学机构ID
     * @param courseDTO 课程基本信息
     * @return 课程基本信息
     */
    CourseBaseInfoDTO createCourseBase(Long companyId, AddOrUpdateCourseDTO courseDTO);
}
