package com.mewp.edu.content.model.dto;

import com.mewp.edu.content.model.po.Teachplan;
import com.mewp.edu.content.model.po.TeachplanMedia;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author mewp
 * @version 1.0
 * @date 2023/8/27 17:44
 */
@Data
public class TeachPlanDTO extends Teachplan {
    /**
     * 课程计划关联的媒资信息
     */
    @ApiModelProperty("课程媒资信息")
    private TeachplanMedia teachplanMedia;

    /**
     * 课程计划子目录
     */
    @ApiModelProperty("课程计划子目录")
    private List<TeachPlanDTO> teachPlanTreeNodes;
}
