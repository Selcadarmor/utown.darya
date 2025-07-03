package com.example.Utown.mapper;

import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import com.example.Utown.model.RestaurantCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface RestaurantCategoryInfoMapper {
    RestaurantCategoryDto restaurantCategoryToDto(RestaurantCategory entity);

    RestaurantCategory restaurantCategoryDtoToEntity(RestaurantCategoryDto dto);
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    RestaurantCategory updateFromDto(RestaurantCategoryDto restaurantCategoryDto, @MappingTarget RestaurantCategory entity);
}