package com.example.Utown.mapper;

import com.example.Utown.dto.clientDTO.RestaurantForClientDto;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.RestaurantCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    @Mapping(source = "fileInfo.path", target = "filePath")
    @Mapping(source = "categories", target = "categoryNames", qualifiedByName = "mapCategoryNames")
    @Mapping(source = "categories", target = "categoryIds", qualifiedByName = "mapCategoryIds")  // <-- добавлено
    @Mapping(source = "delivery.price", target = "deliveryPrice")
    RestaurantForClientDto toRestaurantForClientDto(Restaurant restaurant);

    @Named("mapCategoryNames")
    static Set<String> mapCategoryNames(Set<RestaurantCategory> categories) {
        return categories.stream()
                .map(RestaurantCategory::getName)
                .collect(Collectors.toSet());
    }

    @Named("mapCategoryIds")
    static Set<Long> mapCategoryIds(Set<RestaurantCategory> categories) {
        return categories.stream()
                .map(RestaurantCategory::getId)
                .collect(Collectors.toSet());
    }
}

