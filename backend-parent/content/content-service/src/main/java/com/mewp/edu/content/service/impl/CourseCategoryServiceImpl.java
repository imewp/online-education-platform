package com.mewp.edu.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mewp.edu.content.mapper.CourseCategoryMapper;
import com.mewp.edu.content.model.dto.CourseCategoryTreeDTO;
import com.mewp.edu.content.model.po.CourseCategory;
import com.mewp.edu.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        List<CourseCategoryTreeDTO> courseCategoryTrees = courseCategoryMapper.selectTreeNodes(id);
        List<CourseCategoryTreeDTO> result = new ArrayList<>();
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

        return result;


    }
}
