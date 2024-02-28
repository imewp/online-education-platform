package com.mewp.edu.search.entity.dto;

import com.mewp.edu.common.model.PageResult;
import lombok.Data;

import java.util.List;

/**
 * @author mewp
 * @version 1.0
 * @date 2024/2/28 11:06
 */
@Data
public class SearchPageResultDTO<T> extends PageResult {
    //大分类列表
    List<String> mtList;
    //小分类列表
    List<String> stList;

    public SearchPageResultDTO(List<T> items, long counts, long page, long pageSize) {
        super(counts, page, pageSize, items);
    }
}
