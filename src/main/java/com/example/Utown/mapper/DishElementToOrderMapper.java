package com.example.Utown.mapper;

import com.example.Utown.dto.dishElementToOrderDTO.DishElementToOrderDto;
import com.example.Utown.model.DishElementToOrder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DishElementToOrderMapper {
    DishElementToOrderDto toDto(DishElementToOrder entity);
    DishElementToOrder toEntity(DishElementToOrderDto dto);
}

