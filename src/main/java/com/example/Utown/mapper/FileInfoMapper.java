package com.example.Utown.mapper;

import com.example.Utown.dto.fileInfoDTO.FileInfoDto;
import com.example.Utown.model.FileInfo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FileInfoMapper {
    FileInfo toEntity(FileInfoDto fileInfo);
    FileInfo updateFromDto(FileInfoDto fileInfoDto, @MappingTarget FileInfo fileInfo);
}
