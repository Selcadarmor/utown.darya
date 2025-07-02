package com.example.Utown.mapper;

import com.example.Utown.dto.operatingModeDto.OperatingModeDto;
import com.example.Utown.model.OperatingMode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OperatingModeMapper {
    @Mapping(target = "dayOfWeek", expression = "java(entity.getDayOfWeek())")
    OperatingModeDto toDto(OperatingMode entity);
}
