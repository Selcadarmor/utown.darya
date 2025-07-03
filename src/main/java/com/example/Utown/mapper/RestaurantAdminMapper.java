package com.example.Utown.mapper;

import com.example.Utown.dto.restaurantAdminDTO.RestaurantAdminInfoDto;
import com.example.Utown.model.UserType.RestaurantAdmin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RestaurantAdminMapper {

    @Mapping(target = "active", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    RestaurantAdmin toEntity(RestaurantAdminInfoDto restaurantAdmin);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RestaurantAdmin updateFromDto(RestaurantAdminInfoDto restaurantAdminInfoDto, @MappingTarget RestaurantAdmin restaurantAdmin);
}
