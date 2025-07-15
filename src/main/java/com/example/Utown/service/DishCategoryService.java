package com.example.Utown.service;

import com.example.Utown.dto.dishCategoryDTO.DishCategoryCreateDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryDetailsDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryRestaurantProfileDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DishCategoryService {
    DishCategoryDto createDishCategory(DishCategoryDto dto);
    DishCategoryDto getDishCategoryById(Long id);
    List<DishCategoryDto> getAllDishCategories();
    DishCategoryDto updateDishCategory(Long id, DishCategoryDto dto);
    void deleteDishCategory(Long id);
    Page<DishCategoryDetailsDto> getDishCategoriesByRestaurantId(Long restaurantId, int page, int size);
    DishCategoryDetailsDto createDishCategoryForRestaurant(Long restaurantId, DishCategoryCreateDto dto);
    List<DishCategoryRestaurantProfileDto> getDishCategoriesByRestaurant(Long restaurantId);
}

