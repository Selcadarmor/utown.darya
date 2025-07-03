package com.example.Utown.mapper;

import com.example.Utown.dto.fileInfoDTO.FileInfoDetailsDto;
import com.example.Utown.dto.fileInfoDTO.FileInfoDto;
import com.example.Utown.model.FileInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FileInfoMapper {
    FileInfo toEntity(FileInfoDetailsDto fileInfo);
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FileInfo updateFromDto(FileInfoDetailsDto fileInfoDto, @MappingTarget FileInfo fileInfo);
}
