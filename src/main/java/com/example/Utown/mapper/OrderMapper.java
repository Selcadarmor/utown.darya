package com.example.Utown.mapper;

import com.example.Utown.dto.clientDTO.ClientShortDto;
import com.example.Utown.dto.dishToOrderDTO.DishToOrderResponseDto;
import com.example.Utown.dto.orderDTO.OrderDto;
import com.example.Utown.dto.restaurantDTO.RestaurantShortDto;
import com.example.Utown.model.DishToOrder;
import com.example.Utown.model.Order;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.UserType.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DishToOrderMapper.class})
public interface OrderMapper {

    @Mapping(target = "restaurant", source = "restaurant", qualifiedByName = "toRestaurantShortDto")
    @Mapping(target = "client", source = "client", qualifiedByName = "toClientShortDto")
    @Mapping(target = "dishesToOrder", source = "dishesToOrder")
    OrderDto orderToDto(Order order);

    Order orderDtoToEntity(OrderDto dto);

    @Named("toRestaurantShortDto")
    default RestaurantShortDto toRestaurantShortDto(Restaurant restaurant) {
        if (restaurant == null) return null;
        return new RestaurantShortDto(restaurant.getId(), restaurant.getTitle(), restaurant.getPhone());
    }

    @Named("toClientShortDto")
    default ClientShortDto toClientShortDto(Client client) {
        if (client == null) return null;
        return new ClientShortDto(
                client.getId(),
                client.getFullName(),
                client.getUsername()
        );
    }
}

