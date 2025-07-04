package com.example.Utown.mapper;

import com.example.Utown.dto.orderDTO.OrderInfoDto;
import com.example.Utown.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {
        RestaurantInfoMapper.class,
})
public interface OrderInfoMapper {

    @Mapping(target = "dishesToOrder", ignore = true)
    @Mapping(target = "dishElementToOrders", ignore = true)
    Order toEntity(OrderInfoDto dto);

    @Mapping(target = "dishesToOrder", ignore = true)
    @Mapping(target = "dishElementToOrders", ignore = true)
    Order updateFromDto(OrderInfoDto orderInfoDto, @MappingTarget Order order);
}
