package com.example.Utown.mapper;

import com.example.Utown.dto.dishDTO.DishDto;
import com.example.Utown.dto.dishDTO.DishForClientDto;
import com.example.Utown.dto.dishDTO.DishInfoDto;
import com.example.Utown.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DishMapper {

    @Mapping(source = "dishCategory.id", target = "dishCategoryId")
    @Mapping(source = "file.id", target = "fileId")
    DishDto dishToDto(Dish dish);

    @Named("dishUpdateInfoDto")
    @Mapping(source = "dishCategory.id", target = "dishCategoryId")
    @Mapping(source = "file.id", target = "fileId")
    DishInfoDto dishUpdateInfoToDto(Dish dish);

    @Named("toSavedDishDto")
    @Mapping(source = "dishCategory.id", target = "dishCategoryId")
    @Mapping(source = "file.id", target = "fileId")
    DishInfoDto toSavedDishDto(Dish dish);

    @Mapping(source = "dishCategoryId", target = "dishCategory.id")
    //@Mapping(source = "fileId", target = "file.id")
    @Mapping(source = "options", target = "options")
    Dish toEntity(DishInfoDto dto);


    @Mapping(target = "dishCategoryId", source = "dishCategory.id")
    @Mapping(target = "filePath", source = "file.path")
    @Mapping(target = "restaurantId", source = "restaurant.id")
    DishForClientDto dishToClientDto(Dish dish);

    @Mapping(target = "dishCategoryId", source = "dishCategory.id")
    @Mapping(target = "filePath", source = "file.path")
    @Mapping(target = "restaurantId", source = "restaurant.id")
    DishForClientDto dishToSearchDto(Dish dish);

}

