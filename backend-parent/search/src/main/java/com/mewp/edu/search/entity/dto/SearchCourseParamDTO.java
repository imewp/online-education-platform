package com.mewp.edu.search.entity.dto;

import lombok.Data;

/**
 * @author mewp
 * @version 1.0
 * @date 2024/2/28 11:05
 */
@Data
public class SearchCourseParamDTO {
    //关键字
    private String keywords;

    //大分类
    private String mt;

    //小分类
    private String st;

    //难度等级
    private String grade;
}
