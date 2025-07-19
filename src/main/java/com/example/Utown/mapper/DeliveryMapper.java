package com.example.Utown.mapper;

import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import com.example.Utown.dto.deliveryDTO.DeliveryInfoDto;
import com.example.Utown.model.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    @Mapping(source = "restaurant.id", target = "restaurantId")
    DeliveryDto deliveryToDto(Delivery delivery);

    List<DeliveryInfoDto> toDtoList(List<Delivery> deliveries);

    @Mapping(source = "restaurant.id", target = "restaurantId")
    DeliveryInfoDto toDto(Delivery delivery);
}



