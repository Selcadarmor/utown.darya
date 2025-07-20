package com.example.Utown.mapper;

import com.example.Utown.dto.fileInfoDTO.FileInfoDetailsDto;
import com.example.Utown.dto.fileInfoDTO.FileInfoDto;
import com.example.Utown.model.FileInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileInfoMapper {
    FileInfo toEntity(FileInfoDto fileInfo);
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FileInfo updateFromDto(FileInfoDto fileInfoDto, @MappingTarget FileInfo fileInfo);
    FileInfoDto toDto(FileInfo entity);
    FileInfo toDetailsEntity(FileInfoDetailsDto fileInfo);
}

