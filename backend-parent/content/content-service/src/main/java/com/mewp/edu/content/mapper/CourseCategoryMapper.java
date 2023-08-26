package com.mewp.edu.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mewp.edu.content.handler.MapResultHandler;
import com.mewp.edu.content.model.dto.CourseCategoryTreeDTO;
import com.mewp.edu.content.model.po.CourseCategory;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程分类 Mapper 接口
 * </p>
 *
 * @author mewp
 */
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {

    /**
     * 通过递归方式获取课程分类的树形结构
     *
     * @param id 分类Id
     * @return 课程分类的树形结构¬
     */
    List<CourseCategoryTreeDTO> selectTreeNodes(@Param("id") String id);

    /**
     * 批量查询课程名称
     *
     * @param ids 课程id集合
     */
    void findByIds(MapResultHandler<String, String> resultHandler,
                   @Param("list") List<String> ids);
}
