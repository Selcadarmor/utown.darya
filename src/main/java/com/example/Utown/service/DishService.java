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
    List<DishDto> getAllDishes();
    Dish updateDish(Long id, DishDto dto);
    void deleteDish(Long id);
    Page<DishDetailsDto> getDishesByRestaurantId(Long restaurantId, int page, int size);
    DishInfoDto updateDishForRestaurant(Long RestaurantId, Long DishId, DishCreateDto dto);
    DishInfoDto createDishForRestaurant(Long RestaurantId, DishCreateDto dto);
    DishDetailsDto updateDishForRestaurant(Long RestaurantId, Long DishId, DishDetailsDto dto);
    DishDetailsDto createDishForRestaurant(Long RestaurantId, DishDetailsDto dto);
    DishForClientDto getDishByIdForClient(Long dishId);
    List<DishForClientDto> getDishesByCategoryForClient(Long categoryId);
}

