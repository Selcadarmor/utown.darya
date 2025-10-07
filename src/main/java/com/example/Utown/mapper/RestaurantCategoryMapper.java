package com.example.Utown.mapper;

import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryCreateDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import com.example.Utown.model.RestaurantCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantCategoryMapper {

    RestaurantCategory toEntity(RestaurantCategoryCreateDto dto);

    RestaurantCategoryDto toDto(RestaurantCategory saved);
    @Mapping(target = "name", source = "name")
    @Mapping(target = "sort", source = "sort")
    @Mapping(target = "isActive", source = "isActive")
    void updateFromDto(RestaurantCategoryCreateDto dto,  @MappingTarget RestaurantCategory category);
}

