package com.example.Utown.service;

import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryForClient;
import com.example.Utown.model.RestaurantCategory;
import java.util.List;

public interface RestaurantCategoryService {
    RestaurantCategory createRestaurantCategory(RestaurantCategoryDto dto);
    RestaurantCategoryDto getRestaurantCategoryById(Long id);
    List<RestaurantCategoryDto> getAllRestaurantCategories();
    RestaurantCategory updateRestaurantCategory(Long id, RestaurantCategoryDto dto);
    void deleteRestaurantCategory(Long id);
    List<RestaurantCategoryForClient> getAllCategoriesWithCount();
}

