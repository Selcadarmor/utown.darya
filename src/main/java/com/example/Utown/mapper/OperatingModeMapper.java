package com.example.Utown.mapper;

import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.model.OperatingMode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OperatingModeMapper {
    OperatingModeInfoDto toDto(OperatingMode entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dayOfWeek", source = "dayOfWeek")
    @Mapping(target = "start", source = "start")
    @Mapping(target = "end", source = "end")
    @Mapping(target = "dayOff", source = "dayOff")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OperatingMode updateFromDto(OperatingModeInfoDto dto, @MappingTarget OperatingMode entity);
}
