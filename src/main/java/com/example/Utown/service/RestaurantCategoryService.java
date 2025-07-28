package com.example.Utown.service;

import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryCreateDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryForClient;
import com.example.Utown.model.RestaurantCategory;

import java.util.List;

public interface RestaurantCategoryService {
    RestaurantCategory getRestaurantCategoryById(Long id);

    List<RestaurantCategory> getAllRestaurantCategories();

    List<RestaurantCategoryForClient> getAllCategoriesWithRestaurantCount();

    RestaurantCategoryDto createCategory(RestaurantCategoryCreateDto dto); //cделать контроллер

    RestaurantCategoryDto updateRestaurantCategory(Long id, RestaurantCategoryCreateDto dto); //сделать контроллер

    void deleteRestaurantCategory(Long id); // сделать энпоинт
}