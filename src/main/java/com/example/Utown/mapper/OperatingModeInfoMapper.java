package com.example.Utown.mapper;

import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.model.OperatingMode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OperatingModeInfoMapper {
    @Mapping(source = "restaurant.id", target = "restaurantId")
    OperatingModeInfoDto toDto(OperatingMode mode);

    List<OperatingModeInfoDto> toDtoList(List<OperatingMode> operatingModes);

}
