package com.example.Utown.mapper;

import com.example.Utown.dto.restaurantDTO.RestaurantCreateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.model.Delivery;
import com.example.Utown.model.OperatingMode;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.RestaurantCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", uses = {AddressInfoMapper.class, RestaurantCategoryInfoMapper.class, OrderInfoMapper.class, RestaurantAdminInfoMapper.class, DishCategoryMapper.class})
public interface RestaurantInfoMapper {

    @Mapping(source = "fileInfo.id", target = "fileId")
    @Mapping(source = "categories", target = "categoryIds")
    @Mapping(source = "operatingModes", target = "operatingModeIds")
    @Mapping(source = "restaurantAdmin.id", target = "restaurantAdminId")
    @Mapping(target = "orderCount", ignore = true)
    @Mapping(source = "deliveries", target = "deliveryIds")
    RestaurantDetailsDto toDto(Restaurant restaurant);


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
            @Mapping(target = "operatingModes", ignore = true)
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
            @Mapping(target = "updatedAt", ignore = true)
    })
    Restaurant toEntity(RestaurantCreateDto dto);//+


    default List<Long> mapCategories(Set<RestaurantCategory> categories) {
        if (categories == null) return List.of();
        return categories.stream()
                .map(RestaurantCategory::getId)
                .toList();
    }


    default List<Long> mapDeliveriesToIds(List<Delivery> deliveries) {
        if (deliveries == null) {
            return null;
        }
        return deliveries.stream()
                .map(Delivery::getId)
                .collect(Collectors.toList());
    }

    default List<Long> mapOperatingModes(List<OperatingMode> operatingModes) {
        if (operatingModes == null) return List.of();
        return operatingModes.stream()
                .map(OperatingMode::getId)
                .toList();
    }


}
