package com.example.Utown.mapper;

import com.example.Utown.dto.dishCategoryDTO.DishCategoryDetailsDto;
import com.example.Utown.model.DishCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DishCategoryMapper {

    DishCategoryDetailsDto toDetailsDto(DishCategory category);
    @Mapping(target = "id", ignore = true)
    DishCategoryDetailsDto toCreateDto(DishCategory category);

}
