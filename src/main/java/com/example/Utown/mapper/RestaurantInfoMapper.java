package com.example.Utown.mapper;

import com.example.Utown.dto.restaurantDTO.RestaurantCreateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateResponseDto;
import com.example.Utown.dto.restaurantDTO.RestaurantsCreateResponseDto;
import com.example.Utown.model.Restaurant;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", uses = { RestaurantCategoryInfoMapper.class,
        RestaurantAdminInfoMapper.class, DishCategoryMapper.class, DeliveryMapper.class, OperatingModeInfoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RestaurantInfoMapper {
    @Mapping(source = "fileInfo.id", target = "fileId")
    @Mapping(target = "orderCount", ignore = true)
    @Mapping(source = "categories", target = "categories")
    @Mapping(source = "operatingModes", target = "operatingModes")
    @Mapping(source = "deliveries", target = "deliveries")
    RestaurantDetailsDto toDto(Restaurant restaurant);

    @Mapping(source = "fileInfo", target = "file")
    @Mapping(source = "categories", target = "categories")
    @Mapping(source = "operatingModes", target = "operatingModes")
    @Mapping(source = "deliveries", target = "deliveries")
    RestaurantsCreateResponseDto toCreateDto(Restaurant restaurant);

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
            @Mapping(target = "deliveries", ignore = true),
            @Mapping(target = "dishCategories", ignore = true),
            @Mapping(target = "restaurantAdmin", ignore = true),
            @Mapping(target = "operatingModes", ignore = true),
            @Mapping(target = "categories", ignore = true),
            @Mapping(target = "fileInfo", ignore = true)
    })//+
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
            @Mapping(target = "deliveries", ignore = true),
            @Mapping(target = "operatingModes", ignore = true),
            @Mapping(target = "fileInfo", ignore = true),
            @Mapping(target = "dishCategories", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    Restaurant toEntity(RestaurantCreateDto dto);//+

    @Mappings({
            @Mapping(target = "deliveryTime", ignore = true),
            @Mapping(target = "facilities", ignore = true),
            @Mapping(target = "isRecommended", ignore = true),
            @Mapping(target = "rating", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "totalRatings", ignore = true),
            @Mapping(target = "statusForcedChanged", ignore = true),
            @Mapping(target = "address", ignore = true),
            @Mapping(target = "fileInfo", ignore = true),
            @Mapping(target = "dishCategories", ignore = true),
            @Mapping(target = "restaurantAdmin", ignore = true),
            @Mapping(target = "deliveries", ignore = true),
            @Mapping(target = "operatingModes", ignore = true),
            @Mapping(target = "isActive", ignore = true),
    })
    Restaurant toEntity(RestaurantDetailsDto dto);

    @Mapping(source = "fileInfo", target = "file")
    @Mapping(source = "categories", target = "categories")
    @Mapping(source = "operatingModes", target = "operatingModes")
    @Mapping(source = "deliveries", target = "deliveries")
    RestaurantUpdateResponseDto toUpdateDto(Restaurant restaurant);

}
