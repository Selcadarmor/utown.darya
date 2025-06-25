package com.example.Utown.service;

import com.example.Utown.dto.clientDto.RestaurantCategoryDto;

import java.util.List;

public interface RestaurantCategoryService {
    List<RestaurantCategoryDto> getAllCategoriesWithCount();
}
