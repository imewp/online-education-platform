package com.mewp.edu.search.service;


import com.mewp.edu.common.param.PageParams;
import com.mewp.edu.search.entity.dto.SearchCourseParamDTO;
import com.mewp.edu.search.entity.dto.SearchPageResultDTO;
import com.mewp.edu.search.entity.po.CourseIndex;

/**
 * 课程搜索service
 *
 * @author Mr.M
 * @version 1.0
 * @date 2022/9/24 22:40
 */
public interface CourseSearchService {
    /**
     * 搜索课程列表
     *
     * @param pageParams           分页参数
     * @param searchCourseParamDto 搜索条件
     * @return 课程列表
     */
    SearchPageResultDTO<CourseIndex> queryCoursePubIndex(PageParams pageParams, SearchCourseParamDTO searchCourseParamDto);
}
