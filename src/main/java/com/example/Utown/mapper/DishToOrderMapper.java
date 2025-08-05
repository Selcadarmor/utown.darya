package com.example.Utown.mapper;

import com.example.Utown.dto.dishToOrderDTO.DishToOrderResponseDto;
import com.example.Utown.model.DishToOrder;
import com.example.Utown.model.Element;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DishToOrderMapper {

    @Named("getElementNames")
    default List<String> getElementNames(DishToOrder entity) {
        if (entity.getSelectedElements() == null) return List.of();
        return entity.getSelectedElements().stream()
                .map(Element::getName)
                .toList();
    }
}


