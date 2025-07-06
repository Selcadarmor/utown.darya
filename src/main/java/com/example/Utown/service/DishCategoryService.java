package com.example.Utown.service;

import com.example.Utown.dto.dishCategoryDTO.DishCategoryCreateDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryDetailsDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryDto;
import com.example.Utown.model.DishCategory;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DishCategoryService {

    DishCategory createDishCategory(DishCategoryDto dto);
    DishCategoryDto getDishCategoryById(Long id);
    List<DishCategoryDto> getAllDishCategories();
    DishCategory updateDishCategory(Long id, DishCategoryDto dto);
    void deleteDishCategory(Long id);
    Page<DishCategoryDetailsDto> getDishCategoriesByRestaurantId(Long restaurantId, int page, int size);
    DishCategoryDetailsDto createDishCategoryForRestaurant(Long RestaurantId, DishCategoryCreateDto dto);
}

