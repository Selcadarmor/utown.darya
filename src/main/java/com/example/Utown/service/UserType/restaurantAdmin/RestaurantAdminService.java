package com.example.Utown.service.UserType.restaurantAdmin;

import com.example.Utown.dto.restaurantAdminDto.RestaurantAdminCreateDto;
import com.example.Utown.dto.restaurantAdminDto.RestaurantAdminDto;
import com.example.Utown.dto.restaurantAdminDto.RestaurantAdminUpdateDto;

import java.util.List;

public interface RestaurantAdminService {
    /// администратор рестаранов
    RestaurantAdminCreateDto createAdmin(RestaurantAdminCreateDto dto);
    List<RestaurantAdminDto> getAllRestaurantAdmins();
    RestaurantAdminDto getRestaurantAdminById(Long id);
    RestaurantAdminUpdateDto updateRestaurantAdmin(Long id, RestaurantAdminUpdateDto dto);
    void deleteRestaurantAdmin(Long id);
}

