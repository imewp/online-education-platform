package com.mewp.edu.content.model.dto;

import lombok.Data;

/**
 * 课程查询参数
 *
 * @author mewp
 * @version 1.0
 * @date 2023/8/14 23:28
 */
@Data
public class QueryCourseParamsDTO {
    /**
     * 审核状态
     */
    private String auditStatus;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 发布状态
     */
    private String publishStatus;
}
