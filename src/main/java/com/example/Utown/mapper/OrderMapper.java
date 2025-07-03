package com.example.Utown.mapper;

import com.example.Utown.dto.orderDto.OrderInfoDto;
import com.example.Utown.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toEntity(OrderInfoDto dto);
    Order updateFromDto(OrderInfoDto orderInfoDto, @MappingTarget Order order);
}
