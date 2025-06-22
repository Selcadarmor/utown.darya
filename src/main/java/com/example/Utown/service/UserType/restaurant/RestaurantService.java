package com.example.Utown.service.UserType.restaurant;

import com.example.Utown.dto.RestaurantDto;

import java.util.List;

public interface RestaurantService {
    /// рестораны
    List<RestaurantDto> getAllRestaurants();
    RestaurantDto getRestaurantById(Long id);
    RestaurantDto createRestaurant(RestaurantDto dto);
    RestaurantDto updateRestaurant(Long id, RestaurantDto dto);
    void deleteRestaurant(Long id);
}
