package com.example.Utown.mapper;

import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import com.example.Utown.model.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    @Mapping(source = "restaurant.id", target = "restaurantId")
    DeliveryDto deliveryToDto(Delivery delivery);

    @Mapping(source = "restaurantId", target = "restaurant.id")
    Delivery deliveryDtoToEntity(DeliveryDto dto);
}

