package com.example.Utown.service;

import com.example.Utown.dto.operatingModeDTO.OperatingModeCreateDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeUpdateDto;
import com.example.Utown.model.Restaurant;

import java.util.List;

public interface OperatingModeService {
    OperatingModeInfoDto findById(Long id);
    List<OperatingModeInfoDto> getOperatingModesByRestaurantId(Long restaurantId);
    OperatingModeInfoDto update(Long id, OperatingModeUpdateDto dto);
    void updateOperatingModes(List<OperatingModeUpdateDto> dtos);
    OperatingModeInfoDto createOperatingMode(OperatingModeCreateDto dto);
}
