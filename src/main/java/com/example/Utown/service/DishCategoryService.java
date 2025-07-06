package com.example.Utown.service;

import com.example.Utown.dto.dishCategoryDTO.DishCategoryDto;

import java.util.List;

public interface DishCategoryService {
    DishCategoryDto createDishCategory(DishCategoryDto dto);
    DishCategoryDto getDishCategoryById(Long id);
    List<DishCategoryDto> getAllDishCategories();
    DishCategoryDto updateDishCategory(Long id, DishCategoryDto dto);
    void deleteDishCategory(Long id);
}


