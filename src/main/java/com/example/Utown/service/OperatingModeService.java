package com.example.Utown.service;

import com.example.Utown.dto.operatingModeDTO.OperatingModeCreateDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeUpdateDto;
import com.example.Utown.model.Restaurant;

import java.util.List;

public interface OperatingModeService {
    List<OperatingModeInfoDto> findAll();
    OperatingModeInfoDto findById(Long id);
    OperatingModeInfoDto createOperatingMode(OperatingModeCreateDto dto);
    OperatingModeInfoDto update(Long id, OperatingModeUpdateDto dto);
    List<OperatingModeInfoDto> getOperatingModesByRestaurantId(Long restaurantId);
    void updateOperatingModes(List<OperatingModeUpdateDto> dtos);
}
