package com.example.Utown.service;

import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryForClient;

import java.util.List;

public interface RestaurantCategoryService {
    List<RestaurantCategoryForClient> getAllCategoriesWithCount();
        RestaurantCategoryDto createCategory(RestaurantCategoryDto dto);
        RestaurantCategoryDto getCategoryById(Long id);
        List<RestaurantCategoryDto> getAllCategories();
        RestaurantCategoryDto updateCategory(Long id, RestaurantCategoryDto dto);
        void deleteCategory(Long id);
}
