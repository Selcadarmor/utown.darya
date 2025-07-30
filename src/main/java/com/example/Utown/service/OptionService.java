package com.example.Utown.service;

import com.example.Utown.dto.optionDTO.OptionInfoDto;
import com.example.Utown.model.Dish;
import com.example.Utown.model.Option;
import java.util.Set;

public interface OptionService {
    Option getById(Long id);
    void deleteOption(Long id);
    Set<Option> createOptionsForDish(Dish dish, Set<OptionInfoDto> optionDtos);
    Set<Option> updateOptionsForDish(Dish dish, Set<OptionInfoDto> optionDtos);
    Set<OptionInfoDto> getOptionsWithElementsByDish(Set<Option> options);
}

