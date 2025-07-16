package com.example.Utown.mapper;

import com.example.Utown.dto.orderDTO.OrderInfoDto;
import com.example.Utown.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {
        RestaurantInfoMapper.class,
        DeliveryMapper.class,
        OperatingModeInfoMapper.class,
        RestaurantAdminInfoMapper.class
})
public interface OrderInfoMapper {


    @Mappings({
            @Mapping(target = "deliveryTime", ignore = true),
            @Mapping(target = "details", ignore = true),
            @Mapping(target = "fullAddress", ignore = true),
            @Mapping(target = "isPaid", ignore = true),
            @Mapping(target = "latitude", ignore = true),
            @Mapping(target = "longitude", ignore = true),
            @Mapping(target = "noteForCourier", ignore = true),
            @Mapping(target = "number", ignore = true),
            @Mapping(target = "orderPrice", ignore = true),
            @Mapping(target = "payment", ignore = true),
            @Mapping(target = "postcode", ignore = true),
            @Mapping(target = "restaurantPhone", ignore = true),
            @Mapping(target = "state", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "street", ignore = true),
            @Mapping(target = "time", ignore = true),
            @Mapping(target = "timeOfAccepted", ignore = true),
            @Mapping(target = "timeOfDelivery", ignore = true),
            @Mapping(target = "timeOfSending", ignore = true),
            @Mapping(target = "totalSum", ignore = true),
            @Mapping(target = "typeAddress", ignore = true),
            @Mapping(target = "cookingTime", ignore = true),
            @Mapping(target = "deliveryStatus", ignore = true),
            @Mapping(target = "endTimeOfCooking", ignore = true),
            @Mapping(target = "intercomCode", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "city", ignore = true),
            @Mapping(target = "clientPhone", ignore = true),
            @Mapping(target = "date", ignore = true),
            @Mapping(target = "deliveryPrice", ignore = true),
            @Mapping(target = "restaurant", ignore = true),
            @Mapping(target = "client", ignore = true),
            @Mapping(target = "dishesToOrder", ignore = true),
    })
    Order toEntity(OrderInfoDto dto);


    @Mappings({
            @Mapping(target = "city", ignore = true),
            @Mapping(target = "clientPhone", ignore = true),
            @Mapping(target = "date", ignore = true),
            @Mapping(target = "deliveryPrice", ignore = true),
            @Mapping(target = "deliveryTime", ignore = true),
            @Mapping(target = "details", ignore = true),
            @Mapping(target = "fullAddress", ignore = true),
            @Mapping(target = "isPaid", ignore = true),
            @Mapping(target = "latitude", ignore = true),
            @Mapping(target = "longitude", ignore = true),
            @Mapping(target = "noteForCourier", ignore = true),
            @Mapping(target = "number", ignore = true),
            @Mapping(target = "orderPrice", ignore = true),
            @Mapping(target = "payment", ignore = true),
            @Mapping(target = "postcode", ignore = true),
            @Mapping(target = "restaurantPhone", ignore = true),
            @Mapping(target = "state", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "street", ignore = true),
            @Mapping(target = "time", ignore = true),
            @Mapping(target = "timeOfAccepted", ignore = true),
            @Mapping(target = "timeOfDelivery", ignore = true),
            @Mapping(target = "timeOfSending", ignore = true),
            @Mapping(target = "totalSum", ignore = true),
            @Mapping(target = "typeAddress", ignore = true),
            @Mapping(target = "cookingTime", ignore = true),
            @Mapping(target = "deliveryStatus", ignore = true),
            @Mapping(target = "endTimeOfCooking", ignore = true),
            @Mapping(target = "intercomCode", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "client", ignore = true),
            @Mapping(target = "dishesToOrder", ignore = true),
            @Mapping(source = "restaurantDto", target = "restaurant")
    })

    Order updateFromDto(OrderInfoDto orderInfoDto, @MappingTarget Order order);
}