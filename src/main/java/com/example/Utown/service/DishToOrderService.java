package com.example.Utown.service;

import com.example.Utown.dto.dishToOrderDTO.DishToOrderDto;
import com.example.Utown.model.DishToOrder;

import java.util.List;

public interface DishToOrderService {
    DishToOrder create(DishToOrderDto dto);
    DishToOrderDto getById(Long id);
    List<DishToOrderDto> getAll();
    DishToOrder update(Long id, DishToOrderDto dto);
    void delete(Long id);
}

