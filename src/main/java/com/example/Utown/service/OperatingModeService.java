package com.example.Utown.service;

import com.example.Utown.dto.operatingModeDTO.OperatingModeCreateDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeUpdateDto;


import java.util.List;

public interface OperatingModeService {
    List<OperatingModeInfoDto> findAll();
    OperatingModeInfoDto findById(Long id);
    void deleteAllByRestaurantId(Long restaurantId);
    OperatingModeInfoDto create(OperatingModeCreateDto dto);
    OperatingModeInfoDto update(Long id, OperatingModeUpdateDto dto);
    List<OperatingModeInfoDto> createAll(List<OperatingModeCreateDto> dtos);
}
