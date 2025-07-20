package com.example.Utown.service;

import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryForClient;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryInfoDto;
import com.example.Utown.model.RestaurantCategory;
import java.util.List;

public interface RestaurantCategoryService {
    List<RestaurantCategory> createRestaurantCategories(List<RestaurantCategoryDto> dtos);

    RestaurantCategoryDto getRestaurantCategoryById(Long id);

    List<RestaurantCategoryDto> getAllRestaurantCategories();

    RestaurantCategory updateRestaurantCategory(Long id, RestaurantCategoryDto dto);

    void deleteRestaurantCategory(Long id);

    List<RestaurantCategoryForClient> getAllCategoriesWithRestaurantCount();

    RestaurantCategory updateRestaurantCategoryForRestaurant(Long id, RestaurantCategoryInfoDto categoriesDto);

    List<RestaurantCategory> findCategoriesByIds(List<RestaurantCategoryDto> dtos);

}