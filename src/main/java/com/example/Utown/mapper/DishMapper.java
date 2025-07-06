package com.example.Utown.mapper;

import com.example.Utown.dto.dishDTO.DishDetailsDto;
import com.example.Utown.dto.dishDTO.DishDto;
import com.example.Utown.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DishMapper {

    @Mapping(source = "restaurant.id", target = "restaurantId")
    @Mapping(source = "dishCategory.id", target = "dishCategoryId")
    @Mapping(source = "file.id", target = "fileId")
    DishDto dishToDto(Dish dish);

    @Mapping( target = "fileId", ignore = true)
    @Mapping(source = "restaurant.id", target = "restaurantId")
    @Mapping(source = "dishCategory.id", target = "dishCategoryId")
    DishDetailsDto dishDetailsToDto(Dish dish);

    @Mapping(source = "restaurant.id", target = "restaurantId")
    @Mapping(source = "dishCategory.id", target = "dishCategoryId")
    @Mapping(source = "file.id", target = "fileId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    DishDetailsDto dishUpdateDetailsToDto(Dish dish);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "restaurant.id", target = "restaurantId")
    @Mapping(source = "dishCategory.id", target = "dishCategoryId")
    @Mapping(source = "file.id", target = "fileId")
    DishDetailsDto toSavedDishDto(Dish dish);
}

