package com.example.Utown.mapper;

import com.example.Utown.dto.cartDTO.CartDto;
import com.example.Utown.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DishToOrderMapper.class})
public interface CartMapper {

    @Mapping(target = "dishToOrders", ignore = true)
    @Mapping(target = "client", ignore = true)

    CartDto cartToDto(Cart cart);
    Cart cartDtoToEntity(CartDto cartDto);
}
