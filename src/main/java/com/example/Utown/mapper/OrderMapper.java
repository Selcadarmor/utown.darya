package com.example.Utown.mapper;

import com.example.Utown.dto.orderDTO.OrderDto;
import com.example.Utown.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto orderToDto(Order order);
    Order orderDtoToEntity(OrderDto dto);
}

