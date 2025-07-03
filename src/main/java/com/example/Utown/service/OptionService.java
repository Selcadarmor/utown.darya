package com.example.Utown.service;

import com.example.Utown.dto.optionDTO.OptionDto;
import com.example.Utown.model.Option;

import java.util.List;

public interface OptionService {
    Option create(OptionDto dto);
    OptionDto getById(Long id);
    List<OptionDto> getAll();
    Option update(Long id, OptionDto dto);
    void delete(Long id);
}

