package com.example.Utown.mapper;

import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryInfoDto;
import com.example.Utown.model.RestaurantCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantCategoryInfoMapper {
    RestaurantCategoryDto toDto(RestaurantCategory entity);

    Set<RestaurantCategoryInfoDto> toDtoSet(Set<RestaurantCategory> entities);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RestaurantCategory toEntity(RestaurantCategoryDto restaurantCategoryDto);
}