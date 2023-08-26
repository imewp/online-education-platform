package com.mewp.edu.content.test;

import com.mewp.edu.content.model.dto.CourseCategoryTreeDTO;
import com.mewp.edu.content.service.CourseCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mewp
 * @version 1.0
 * @date 2023/8/26 10:55
 */
@SpringBootTest
public class CourseCategoryTest {
    @Resource
    private CourseCategoryService courseCategoryService;


    @Test
    public void testCourseCategoryTree() {
        List<CourseCategoryTreeDTO> courseCategoryTreeDTOS = courseCategoryService.queryTreeNodes("1");
        System.out.println(courseCategoryTreeDTOS);
    }

}
