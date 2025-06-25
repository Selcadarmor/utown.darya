package com.example.Utown.mapper;

import com.example.Utown.dto.restaurantAdmin.RestaurantAdminCreateDto;
import com.example.Utown.dto.restaurantAdmin.RestaurantAdminDto;
import com.example.Utown.dto.restaurantAdmin.RestaurantAdminUpdateDto;
import com.example.Utown.model.UserType.RestaurantAdmin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantAdminMapper {
    RestaurantAdminDto toDto(RestaurantAdmin restaurantAdmin);
    RestaurantAdmin toRestaurantAdmin(RestaurantAdminDto restaurantAdminDto);

    RestaurantAdminCreateDto toRestaurantAdminCreateDto(RestaurantAdmin restaurantAdmin);
    RestaurantAdmin toRestaurantAdmin(RestaurantAdminCreateDto restaurantAdminCreateDto);

    void updateRestaurantAdminFromDto(RestaurantAdminUpdateDto restaurantAdminUpdateDto, RestaurantAdmin restaurantAdmin);

}
