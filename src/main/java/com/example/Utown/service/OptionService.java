package com.example.Utown.service;

import com.example.Utown.dto.optionDTO.OptionDto;
import com.example.Utown.dto.optionDTO.OptionInfoDto;
import com.example.Utown.model.Dish;
import com.example.Utown.model.Option;

import java.util.List;
import java.util.Set;

public interface OptionService {
    Option create(OptionDto dto);
    OptionDto getById(Long id);
    List<OptionDto> getAll();
    Option update(Long id, OptionDto dto);
    void delete(Long id);
    Set<Option> createOptionsForDish(Dish dish, Set<OptionInfoDto> optionDtos);
    Set<Option> updateOptionsForDish(Dish dish, Set<OptionInfoDto> optionDtos);
}

