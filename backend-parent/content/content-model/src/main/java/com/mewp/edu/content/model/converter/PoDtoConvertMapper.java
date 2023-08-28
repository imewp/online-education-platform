package com.mewp.edu.content.model.converter;

import com.mewp.edu.content.model.dto.AddOrUpdateCourseDTO;
import com.mewp.edu.content.model.dto.AddOrUpdateTeachPlanDTO;
import com.mewp.edu.content.model.dto.CourseBaseInfoDTO;
import com.mewp.edu.content.model.dto.CourseCategoryTreeDTO;
import com.mewp.edu.content.model.po.CourseBase;
import com.mewp.edu.content.model.po.CourseCategory;
import com.mewp.edu.content.model.po.CourseMarket;
import com.mewp.edu.content.model.po.Teachplan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * po与dto的转换器
 *
 * @author mewp
 * @version 1.0
 * @date 2023/8/26 13:16
 */
@Mapper
public interface PoDtoConvertMapper {
    PoDtoConvertMapper INSTANCE = Mappers.getMapper(PoDtoConvertMapper.class);

    CourseBase courseBaseDto2Po(AddOrUpdateCourseDTO courseDto);

    CourseMarket courseMarketDto2Po(AddOrUpdateCourseDTO courseDto);

    @Mapping(target = "id", source = "courseBase.id")
    CourseBaseInfoDTO courseBaseAndMarketPo2Dto(CourseBase courseBase, CourseMarket courseMarket);

    CourseCategoryTreeDTO courseCategoryPo2TreeDto(CourseCategory courseCategory);

    List<CourseCategoryTreeDTO> courseCategoryPos2TreeDtoList(List<CourseCategory> courseCategories);

    Teachplan teachPlanDto2Po(AddOrUpdateTeachPlanDTO teacherDto);
}

