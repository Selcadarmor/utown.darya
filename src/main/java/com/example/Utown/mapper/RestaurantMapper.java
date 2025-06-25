package com.example.Utown.mapper;

import com.example.Utown.dto.clientDto.RestaurantDto;
import com.example.Utown.model.Restaurant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    Restaurant toEntity(RestaurantDto restaurantDto);
    RestaurantDto toDto(Restaurant restaurant);
    void updateRestaurantFromDto(RestaurantDto restaurantDto, Restaurant restaurant);
}
