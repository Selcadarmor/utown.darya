package com.example.Utown.mapper;

import com.example.Utown.dto.RestaurantDto;
import com.example.Utown.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    RestaurantDto toDto(Restaurant restaurant);
    Restaurant toRestaurant(RestaurantDto dto);

    Restaurant toEntity(RestaurantDto dto);
    void updateFromDto(RestaurantDto dto, @MappingTarget Restaurant restaurant);

}
