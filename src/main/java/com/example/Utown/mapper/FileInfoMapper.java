package com.example.Utown.mapper;

import com.example.Utown.dto.fileInfoDTO.FileInfoDto;
import com.example.Utown.model.FileInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileInfoMapper {
    FileInfoDto toDto(FileInfo entity);
    FileInfo toEntity(FileInfoDto dto);
}

