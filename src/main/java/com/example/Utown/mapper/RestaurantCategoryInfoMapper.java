package com.example.Utown.mapper;

import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryInfoDto;
import com.example.Utown.model.RestaurantCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestaurantCategoryInfoMapper {
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    RestaurantCategory updateFromDto(RestaurantCategoryDto restaurantCategoryDto, @MappingTarget RestaurantCategory entity);

    RestaurantCategoryDto toDto(RestaurantCategory entity);

    List<RestaurantCategoryInfoDto> toDtoList(List<RestaurantCategory> entities);
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RestaurantCategory toEntity(RestaurantCategoryDto restaurantCategoryDto);
    @Mapping(target = "id", ignore = true) // id не меняем
    @Mapping(target = "file", ignore = true) // файл обновляем отдельно
    void updateEntityFromDto(RestaurantCategoryDto dto, @MappingTarget RestaurantCategory entity);
}