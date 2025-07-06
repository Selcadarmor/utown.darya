package com.example.Utown.mapper;


import com.example.Utown.dto.dishCategoryDTO.DishCategoryDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryDetailsDto;
import com.example.Utown.model.DishCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DishCategoryMapper {

    @Mapping(source = "restaurant.id", target = "restaurantId")
    DishCategoryDto dishCategoryToDto(DishCategory entity);
    @Mapping(source = "restaurantId", target = "restaurant.id")
    DishCategory dishCategoryDtoToEntity(DishCategoryDto dto);


    DishCategoryDetailsDto toDetailsDto(DishCategory category);
    @Mapping(target = "id", ignore = true)
    DishCategoryDetailsDto toCreateDto(DishCategory category);


}
