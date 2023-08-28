package com.mewp.edu.content.test;

import com.mewp.edu.content.mapper.TeachplanMapper;
import com.mewp.edu.content.model.dto.TeachPlanDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mewp
 * @version 1.0
 * @date 2023/8/27 21:42
 */
@SpringBootTest
public class TeachPlanTest {
    @Resource
    private TeachplanMapper teachplanMapper;

    @Test
    public void testSelectTreeNodes(){
        List<TeachPlanDTO> dtoList = teachplanMapper.selectTreeNodes(117L);
        System.out.println(dtoList);
    }
}
