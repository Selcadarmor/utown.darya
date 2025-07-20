package com.example.Utown.mapper;


import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import com.example.Utown.model.RestaurantCategory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantCategoryMapper {
    RestaurantCategoryDto restaurantCategoryToDto(RestaurantCategory entity);
    RestaurantCategory restaurantCategoryDtoToEntity(RestaurantCategoryDto dto);
}

