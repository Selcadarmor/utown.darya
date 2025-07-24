package com.example.Utown.service;
import com.example.Utown.dto.dishDTO.DishCreateDto;

import com.example.Utown.dto.dishDTO.DishDetailsDto;
import com.example.Utown.dto.dishDTO.DishDto;
import com.example.Utown.dto.dishDTO.DishInfoDto;
import com.example.Utown.dto.dishDTO.DishForClientDto;
import com.example.Utown.model.Dish;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DishService {
    Dish createDish(DishDto dto);
    DishDto getDishById(Long id);
    void deleteDish(Long id);
    Dish updateDish(Long id, DishDto dto);
    Page<DishDetailsDto> getDishesByRestaurantId(Long restaurantId, String title,
                                                 Integer sort, Long dishCategoryId,
                                                 Boolean isActive, int page, int size);
    DishInfoDto updateDishForRestaurant(Long RestaurantId, Long DishId, DishCreateDto dto);
    DishInfoDto createDishForRestaurant(Long RestaurantId, DishCreateDto dto);
    DishForClientDto getDishByIdForClient(Long dishId);
    List<DishForClientDto> getDishesByCategoryForClient(Long categoryId);
}

