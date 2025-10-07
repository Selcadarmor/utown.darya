package com.example.Utown.mapper;

import com.example.Utown.config.S3.AwsProperties;
import com.example.Utown.dto.fileInfoDTO.FileInfoDto;
import com.example.Utown.model.FileInfo;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileInfoMapper {
    FileInfo toEntity(FileInfoDto fileInfo);
    FileInfoDto toDto(FileInfo entity);
    @Mapping(target = "url", expression = "java(buildUrl(fileInfo.getPath(), awsProperties))")
    FileInfoDto toDtoFile(FileInfo fileInfo, @Context AwsProperties awsProperties);

    default String buildUrl(String path, AwsProperties awsProperties) {
        return awsProperties.getPublicBaseUrl() + "/" + path;
    }
}

