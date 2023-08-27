package com.mewp.edu.content.test;

import com.mewp.edu.content.handler.MapResultHandler;
import com.mewp.edu.content.mapper.CourseCategoryMapper;
import com.mewp.edu.content.model.dto.CourseCategoryTreeDTO;
import com.mewp.edu.content.service.CourseCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author mewp
 * @version 1.0
 * @date 2023/8/26 10:55
 */
@SpringBootTest
public class CourseCategoryTest {
    @Resource
    private CourseCategoryService courseCategoryService;
    @Resource
    private CourseCategoryMapper courseCategoryMapper;

    @Test
    public void testCourseCategoryMapperTest() {
        List<CourseCategoryTreeDTO> courseCategoryTreeDTOS = courseCategoryMapper.selectTreeNodes("1");
        System.out.println(courseCategoryTreeDTOS);
    }

    @Test
    public void testCourseCategoryTree() {
        List<CourseCategoryTreeDTO> courseCategoryTreeDTOS = courseCategoryService.queryTreeNodes("1");
        System.out.println(courseCategoryTreeDTOS);
    }

    @Test
    public void testCourseCategoryName() {
        MapResultHandler<String, String> resultHandler = new MapResultHandler<>();
        courseCategoryMapper.findByIds(resultHandler, Arrays.asList("1", "1-1"));
        Map<String, String> map = resultHandler.getMappedResults();
        map.forEach((key, value) -> System.out.println(key + " ----- " + value));
    }

}
