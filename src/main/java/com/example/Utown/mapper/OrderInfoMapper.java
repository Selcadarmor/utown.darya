package com.example.Utown.mapper;

import com.example.Utown.dto.orderDTO.OrderInfoDto;
import com.example.Utown.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {
        RestaurantInfoMapper.class,
})
public interface OrderInfoMapper {


    @Mappings({
            @Mapping(target = "city", ignore = true),
            @Mapping(target = "clientPhone", ignore = true),
            @Mapping(target = "date", ignore = true),
            @Mapping(target = "deliveryPrice", ignore = true),
            @Mapping(target = "dishesToOrder", ignore = true),
            @Mapping(target = "restaurant", ignore = true),
            @Mapping(target = "client", ignore = true)
})
    Order toEntity(OrderInfoDto dto);

    @Mapping(target = "dishesToOrder", ignore = true)
    Order updateFromDto(OrderInfoDto orderInfoDto, @MappingTarget Order order);
}
