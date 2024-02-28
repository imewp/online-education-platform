package com.mewp.edu.search.service;


import com.mewp.edu.common.param.PageParams;
import com.mewp.edu.search.entity.dto.SearchCourseParamDTO;
import com.mewp.edu.search.entity.dto.SearchPageResultDTO;
import com.mewp.edu.search.entity.po.CourseIndex;

/**
 * @author Mr.M
 * @version 1.0
 * @description 课程搜索service
 * @date 2022/9/24 22:40
 */
public interface CourseSearchService {


    /**
     * @param pageParams           分页参数
     * @param searchCourseParamDto 搜索条件
     * @return com.xuecheng.base.model.PageResult<com.xuecheng.search.po.CourseIndex> 课程列表
     * @description 搜索课程列表
     * @author Mr.M
     * @date 2022/9/24 22:45
     */
    SearchPageResultDTO<CourseIndex> queryCoursePubIndex(PageParams pageParams, SearchCourseParamDTO searchCourseParamDto);

}
