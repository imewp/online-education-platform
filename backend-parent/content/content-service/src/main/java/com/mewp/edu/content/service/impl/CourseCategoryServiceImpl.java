package com.mewp.edu.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mewp.edu.content.mapper.CourseCategoryMapper;
import com.mewp.edu.content.model.converter.PoDtoConvertMapper;
import com.mewp.edu.content.model.dto.CourseCategoryTreeDTO;
import com.mewp.edu.content.model.po.CourseCategory;
import com.mewp.edu.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程分类 服务实现类
 * </p>
 *
 * @author mewp
 */
@Slf4j
@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory> implements CourseCategoryService {

    @Resource
    private CourseCategoryMapper courseCategoryMapper;

    @Override
    public List<CourseCategoryTreeDTO> queryTreeNodes(String id) {
        //使用MySQL递归查询数据
        //List<CourseCategoryTreeDTO> courseCategoryTrees = courseCategoryMapper.selectTreeNodes(id);

        //直接查询所有数据
        List<CourseCategory> courseCategories = courseCategoryMapper.selectList(null);
        List<CourseCategoryTreeDTO> courseCategoryTrees =
                PoDtoConvertMapper.INSTANCE.courseCategoryPos2TreeDtoList(courseCategories);

        //第一种方法
       /* List<CourseCategoryTreeDTO> result = new ArrayList<>();
        //将list转成map，用于排除根结点
        Map<String, CourseCategoryTreeDTO> map = courseCategoryTrees.stream()
                .filter(item -> !id.equals(item.getId()))
                .collect(Collectors.toMap(CourseCategory::getId, value -> value, (key1, key2) -> key2));

        //依次遍历每个元素，排除根结点
        courseCategoryTrees.stream()
                .filter(item -> !id.equals(item.getId()))
                .forEach(item -> {
                    if (item.getParentid().equals(id)) {
                        result.add(item);
                    }
                    //找到当前节点的父节点
                    CourseCategoryTreeDTO courseCategoryTreeDTO = map.get(item.getParentid());
                    if (courseCategoryTreeDTO != null) {
                        if (courseCategoryTreeDTO.getChildrenTreeNodes() == null) {
                            courseCategoryTreeDTO.setChildrenTreeNodes(new ArrayList<>());
                        }
                        //往children中放子节点
                        courseCategoryTreeDTO.getChildrenTreeNodes().add(item);
                    }
                });

        return result; */

        //第二种方法
        return buildCourseCategoryTree(courseCategoryTrees, id);
    }

    /**
     * 使用stream流构建课程分类树形结构
     *
     * @param list 课程分类集合
     * @param id   父节点
     * @return 树形结构数据
     */
    public List<CourseCategoryTreeDTO> buildCourseCategoryTree(List<CourseCategoryTreeDTO> list, String id) {
        return list.stream()
                .filter(p -> Objects.equals(p.getParentid(), id))
                .peek(f -> f.setChildrenTreeNodes(buildCourseCategoryTree(list, f.getId())))
                .collect(Collectors.toList());
    }
}
