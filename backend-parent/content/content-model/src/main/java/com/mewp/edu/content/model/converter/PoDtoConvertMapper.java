package com.mewp.edu.content.model.converter;

import com.mewp.edu.content.model.dto.*;
import com.mewp.edu.content.model.po.*;
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

    CourseTeacher courseTeacherDto2Vo(AddOrUpdateCourseTeacherDTO teacherDto);
}

