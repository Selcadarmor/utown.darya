package com.example.Utown.service;

import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryForClient;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryInfoDto;
import com.example.Utown.model.RestaurantCategory;
import java.util.List;
import java.util.Set;

public interface RestaurantCategoryService {
    Set<RestaurantCategory> createRestaurantCategories(Set<RestaurantCategoryDto> dtos);

    RestaurantCategory getRestaurantCategoryById(Long id);

    List<RestaurantCategory> getAllRestaurantCategories();

    RestaurantCategory updateRestaurantCategory(Long id, RestaurantCategoryDto dto);

    void deleteRestaurantCategory(Long id);

    List<RestaurantCategoryForClient> getAllCategoriesWithRestaurantCount();

    Set<RestaurantCategory> findCategoriesByIds(Set<RestaurantCategoryDto> dtos);

    Set<RestaurantCategory> resolveCategories(Set<RestaurantCategoryDto> categoryDtos);
}