package com.mewp.edu.media.model.converter;

import com.mewp.edu.media.model.dto.UploadFileParamsDTO;
import com.mewp.edu.media.model.dto.UploadFileResultDTO;
import com.mewp.edu.media.model.po.MediaFiles;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

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

    MediaFiles uploadFileParamsDto2MediaFiles(UploadFileParamsDTO uploadFileParamsDto);

    UploadFileResultDTO mediaFiles2UploadFileResultDTO(MediaFiles mediaFiles);
}

