package com.mewp.edu.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mewp.edu.content.model.dto.CourseCategoryTreeDTO;
import com.mewp.edu.content.model.po.CourseCategory;

import java.util.List;

/**
 * <p>
 * 课程分类 服务类
 * </p>
 *
 * @author mewp
 * @since 2023-08-20
 */
public interface CourseCategoryService extends IService<CourseCategory> {

    /**
     * 课程分类树形结构查询
     *
     * @param number 指定分类的id
     * @return 树形结构
     */
    List<CourseCategoryTreeDTO> queryTreeNodes(String number);
}
