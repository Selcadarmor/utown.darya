package com.example.Utown.service;

import com.example.Utown.dto.optionDTO.OptionInfoDto;
import com.example.Utown.model.Dish;
import com.example.Utown.model.Option;
import java.util.Set;

public interface OptionService {
    Option getById(Long id);
    Set<OptionInfoDto> getOptionsWithElementsByDish(Set<Option> options);
    Set<Option> createOptionsForDish(Dish dish, Set<OptionInfoDto> optionDtos);
    void deleteOption(Long id);
    void mergeOptions(Dish dish, Set<OptionInfoDto> dtos);
}

