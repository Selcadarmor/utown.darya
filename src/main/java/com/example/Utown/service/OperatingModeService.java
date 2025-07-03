package com.example.Utown.service;

import com.example.Utown.dto.operatingModeDTO.OperatingModeDto;
import com.example.Utown.model.OperatingMode;

import java.util.List;

public interface OperatingModeService {
    OperatingMode create(OperatingModeDto dto);
    OperatingModeDto getById(Long id);
    List<OperatingModeDto> getAll();
    OperatingMode update(Long id, OperatingModeDto dto);
    void delete(Long id);
}

