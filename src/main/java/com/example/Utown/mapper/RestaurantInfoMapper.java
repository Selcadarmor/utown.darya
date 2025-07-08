package com.example.Utown.mapper;

import com.example.Utown.dto.restaurantDTO.RestaurantCreateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.model.OperatingMode;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.RestaurantCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.Set;


@Mapper(componentModel = "spring", uses = {AddressInfoMapper.class, OperatingModeInfoMapper.class, RestaurantCategoryInfoMapper.class, OrderInfoMapper.class, RestaurantAdminInfoMapper.class, DishCategoryMapper.class})
public interface RestaurantInfoMapper {
    @Mappings({
            @Mapping(target = "deliveryTime", ignore = true),
            @Mapping(target = "facilities", ignore = true),
            @Mapping(target = "isRecommended", ignore = true),
            @Mapping(target = "rating", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "totalRatings", ignore = true),
            @Mapping(target = "statusForcedChanged", ignore = true),
            @Mapping(target = "isActive", ignore = true),
            @Mapping(target = "delivery", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(source = "categories", target = "categories")
    })
    Restaurant toEntity(RestaurantCreateDto dto);

    @Mapping(source = "fileInfo.id", target = "fileId")
    @Mapping(source = "categories", target = "categoryIds")
    @Mapping(source = "operatingModes", target = "operatingModeIds")
    @Mapping(source = "restaurantAdmin.id", target = "restaurantAdminId")
    @Mapping(target = "orderCount", ignore = true)
    RestaurantDetailsDto toDto(Restaurant restaurant);

    @Mapping(source = "restaurant.fileInfo.id", target = "fileId")
    @Mapping(source = "restaurant.categories", target = "categoryIds")
    @Mapping(source = "restaurant.operatingModes", target = "operatingModeIds")
    @Mapping(source = "restaurant.restaurantAdmin.id", target = "restaurantAdminId")
    @Mapping(source = "restaurant.id", target = "id")
    @Mapping(source = "restaurant.title", target = "title")
    @Mapping(source = "restaurant.description", target = "description")
    @Mapping(source = "restaurant.phone", target = "phone")
    @Mapping(source = "restaurant.minOrderAmount", target = "minOrderAmount")
    @Mapping(source = "restaurant.createdAt", target = "createdAt")
    @Mapping(source = "restaurant.updatedAt", target = "updatedAt")
    @Mapping(source = "orderCount", target = "orderCount")
    RestaurantDetailsDto toDto(Restaurant restaurant, Long orderCount);


    @Mappings({
            @Mapping(target = "id", ignore = true),
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
            @Mapping(target = "delivery", ignore = true),
            @Mapping(target = "dishCategories", ignore = true)
    })
    Restaurant updateFromDto(RestaurantUpdateDto restaurantUpdateDto, @MappingTarget Restaurant restaurant);

    @Mappings({
            @Mapping(target = "deliveryTime", ignore = true),
            @Mapping(target = "facilities", ignore = true),
            @Mapping(target = "isRecommended", ignore = true),
            @Mapping(target = "rating", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "totalRatings", ignore = true),
            @Mapping(target = "statusForcedChanged", ignore = true),
            @Mapping(target = "isActive", ignore = true),
            @Mapping(target = "restaurantAdmin", ignore = true),
            @Mapping(target = "categories", ignore = true),
            @Mapping(target = "address", ignore = true),
            @Mapping(target = "delivery", ignore = true),
            @Mapping(target = "operatingModes", ignore = true),
            @Mapping(target = "fileInfo", ignore = true),
            @Mapping(target = "dishCategories", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true)
    })
    Restaurant toEntity(RestaurantDetailsDto dto);


    default List<Long> mapCategories(Set<RestaurantCategory> categories) {
        if (categories == null) return List.of();
        return categories.stream()
                .map(RestaurantCategory::getId)
                .toList();
    }

    default List<Long> mapOperatingModes(List<OperatingMode> operatingModes) {
        if (operatingModes == null) return List.of();
        return operatingModes.stream()
                .map(OperatingMode::getId)
                .toList();
    }
}
