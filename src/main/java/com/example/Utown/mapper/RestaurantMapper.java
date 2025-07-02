package com.example.Utown.mapper;

import com.example.Utown.dto.restaurantDto.RestaurantCreateUpdateDto;
import com.example.Utown.model.Restaurant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    Restaurant toEntity(RestaurantCreateUpdateDto dto);
}
