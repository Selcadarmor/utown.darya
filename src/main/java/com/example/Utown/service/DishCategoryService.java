package com.example.Utown.service;

import com.example.Utown.dto.dishCategoryDTO.DishCategoryCreateDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryCreateResponseDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryDetailsDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryRestaurantProfileDto;
import com.example.Utown.model.DishCategory;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DishCategoryService {
    DishCategory getDishCategoryById(Long id);
    List<DishCategoryDto> getAllDishCategories();
    DishCategoryDto updateDishCategory(Long id, DishCategoryDto dto);
    void deleteDishCategory(Long id);
    Page<DishCategoryDetailsDto> getDishCategoriesByRestaurantId(
            Long restaurantId, String query, Integer sort, Boolean isActive, int page, int size);
    DishCategoryCreateResponseDto createDishCategoryForRestaurant(Long restaurantId, DishCategoryCreateDto dto);
    List<DishCategoryRestaurantProfileDto> getDishCategoriesByRestaurantForClient(Long restaurantId);
}

