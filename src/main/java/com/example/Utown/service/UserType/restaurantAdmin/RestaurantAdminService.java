package com.example.Utown.service.UserType.restaurantAdmin;

import com.example.Utown.dto.restaurantAdminDto.RestaurantAdminCreateDto;
import com.example.Utown.dto.restaurantAdminDto.RestaurantAdminDto;
import com.example.Utown.dto.restaurantAdminDto.RestaurantAdminUpdateDto;

import java.util.List;

public interface RestaurantAdminService {
    /// администратор рестаранов
    RestaurantAdminDto createAdmin(RestaurantAdminCreateDto dto);
    List<RestaurantAdminDto> getAllRestaurantAdmins();
    RestaurantAdminDto getRestaurantAdminById(Long id);
    RestaurantAdminDto updateRestaurantAdmin(Long id, RestaurantAdminUpdateDto dto);
    void deleteRestaurantAdmin(Long id);
}

