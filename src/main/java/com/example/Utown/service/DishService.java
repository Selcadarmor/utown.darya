package com.example.Utown.service;
import com.example.Utown.dto.dishDTO.DishCreateDto;

import com.example.Utown.dto.dishDTO.DishDetailsDto;
import com.example.Utown.dto.dishDTO.DishDto;
import com.example.Utown.dto.dishDTO.DishInfoDto;
import com.example.Utown.dto.dishDTO.DishForClientDto;
import com.example.Utown.dto.dishDTO.DishSearchDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.model.Dish;
import com.example.Utown.model.Option;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DishService {
    Dish getDishById(Long id);
    Page<DishDetailsDto> getDishesByRestaurantId(Long restaurantId, String title,
                                                 Integer sort, Long dishCategoryId,
                                                 Boolean isActive, int page, int size);
    DishInfoDto updateDishForRestaurant(Long RestaurantId, Long DishId, DishCreateDto dto);
    DishInfoDto createDishForRestaurant(Long RestaurantId, DishCreateDto dto);
    DishForClientDto getDishByIdForOrder(Long dishId);
    Page<DishSearchDto> searchDishesByRestaurant(Long restaurantId, String keyword, Pageable pageable);
    List<DishSearchDto> getDishesByCategory(Long categoryId);
    void deleteDish(Long id);

}

