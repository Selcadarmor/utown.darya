package com.example.Utown.service;

import com.example.Utown.dto.dishCategoryDTO.DishCategoryDto;
import com.example.Utown.model.DishCategory;

import java.util.List;

public interface DishCategoryService {

    DishCategory createDishCategory(DishCategoryDto dto);
    DishCategoryDto getDishCategoryById(Long id);
    List<DishCategoryDto> getAllDishCategories();
    DishCategory updateDishCategory(Long id, DishCategoryDto dto);
    void deleteDishCategory(Long id);
}

