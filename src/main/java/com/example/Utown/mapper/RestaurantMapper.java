package com.example.Utown.mapper;

import com.example.Utown.dto.restaurantDTO.RestaurantCreateUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;
import com.example.Utown.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring", uses = {AddressMapper.class, OperatingModeMapper.class, RestaurantCategoryMapper.class, OrderMapper.class, RestaurantAdminMapper.class})
public interface RestaurantMapper {
    @Mappings({
            @Mapping(target = "deliveryTime", ignore = true),
            @Mapping(target = "facilities", ignore = true),
            @Mapping(target = "isRecommended", ignore = true),
            @Mapping(target = "rating", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "totalRatings", ignore = true),
            @Mapping(target = "statusForcedChanged", ignore = true),
            @Mapping(target = "isActive", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "restaurantAdmin", ignore = true),
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "orders", ignore = true),
            @Mapping(target = "ratings", ignore = true)
    })
    Restaurant toEntity(RestaurantCreateUpdateDto dto);

    RestaurantDetailsDto toDto(Restaurant restaurant);

    RestaurantInfoDto toRestaurantInfoDto(Restaurant restaurant);

    @Mappings({
            @Mapping(target = "deliveryTime", ignore = true),
            @Mapping(target = "facilities", ignore = true),
            @Mapping(target = "isRecommended", ignore = true),
            @Mapping(target = "rating", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "totalRatings", ignore = true),
            @Mapping(target = "statusForcedChanged", ignore = true),
            @Mapping(target = "isActive", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "restaurantAdmin", ignore = true),
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "orders", ignore = true),
            @Mapping(target = "ratings", ignore = true)
    })
    Restaurant updateFromDto(RestaurantCreateUpdateDto restaurantDetailsDto, @MappingTarget Restaurant restaurant);
}
