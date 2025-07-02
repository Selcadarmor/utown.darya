package com.example.Utown.service.UserType.restaurant;

import com.example.Utown.dto.restaurantDto.RestaurantCreateUpdateDto;
import com.example.Utown.dto.restaurantDto.RestaurantDetailsDto;
import com.example.Utown.dto.restaurantDto.RestaurantInfoDto;

import java.util.List;

public interface RestaurantService {
    List<RestaurantInfoDto> getAllRestaurants();
    RestaurantDetailsDto getRestaurantById(Long id);
    RestaurantDetailsDto createRestaurant(RestaurantCreateUpdateDto dto);
    RestaurantDetailsDto updateRestaurant(Long id, RestaurantCreateUpdateDto dto);
    void deleteRestaurant(Long id);
}
