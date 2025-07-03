package com.example.Utown.mapper;

import com.example.Utown.dto.operatingModeDTO.OperatingModeDto;
import com.example.Utown.model.OperatingMode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OperatingModeMapper {
    OperatingModeDto toDto(OperatingMode entity);
    OperatingMode toEntity(OperatingModeDto dto);
}
