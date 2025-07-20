package com.example.Utown.service.UserType.client;

import com.example.Utown.dto.restaurantAdminDTO.RestaurantAdminCreateDto;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.UserType.RestaurantAdmin;

public interface RestaurantAdminService {
    RestaurantAdmin createAdmin(RestaurantAdminCreateDto dto, Restaurant restaurant);
}
