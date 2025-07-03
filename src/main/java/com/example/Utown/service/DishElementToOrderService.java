package com.example.Utown.service;

import com.example.Utown.dto.dishElementToOrderDTO.DishElementToOrderDto;
import com.example.Utown.model.DishElementToOrder;

import java.util.List;

public interface DishElementToOrderService {
    DishElementToOrder create(DishElementToOrderDto dto);
    DishElementToOrderDto getById(Long id);
    List<DishElementToOrderDto> getAll();
    DishElementToOrder update(Long id, DishElementToOrderDto dto);
    void delete(Long id);
}

