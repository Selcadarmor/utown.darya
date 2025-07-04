package com.example.Utown.service;
import com.example.Utown.dto.dishDTO.DishDto;
import com.example.Utown.model.Dish;

import java.util.List;

public interface DishService {
    Dish createDish(DishDto dto);
    DishDto getDishById(Long id);
    List<DishDto> getAllDishes();
    Dish updateDish(Long id, DishDto dto);
    void deleteDish(Long id);
}

