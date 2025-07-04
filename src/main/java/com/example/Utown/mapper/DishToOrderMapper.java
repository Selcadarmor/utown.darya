package com.example.Utown.mapper;

import com.example.Utown.dto.dishToOrderDTO.DishToOrderDto;
import com.example.Utown.model.DishToOrder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DishToOrderMapper {
    DishToOrderDto toDto(DishToOrder entity);
    DishToOrder toEntity(DishToOrderDto dto);
}

