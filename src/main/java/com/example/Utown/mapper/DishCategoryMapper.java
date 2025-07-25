package com.example.Utown.mapper;

import com.example.Utown.dto.dishCategoryDTO.DishCategoryCreateDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryCreateResponseDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryDetailsDto;
import com.example.Utown.model.DishCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface DishCategoryMapper {

    @Mapping(source = "restaurant.id", target = "restaurantId")
    DishCategoryDto dishCategoryToDto(DishCategory entity);


    @Mapping(source = "restaurantId", target = "restaurant.id")
    @Mapping(target = "dishes", ignore = true)
    DishCategory dishCategoryDtoToEntity(DishCategoryDto dto);

    @Mapping(source = "restaurant.id", target = "restaurantId")
    DishCategoryDetailsDto toDetailsDto(DishCategory category);


    DishCategoryCreateResponseDto toCreateDto(DishCategory category);

    @Mappings({
            @Mapping(target = "isActive", constant = "true"),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "restaurant", ignore = true),
            @Mapping(target = "file", ignore = true),
            @Mapping(target = "dishes", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    DishCategory toEntity(DishCategoryCreateDto dto);

}
