package com.example.Utown.mapper;

import com.example.Utown.dto.cartDTO.CartDto;
import com.example.Utown.model.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto cartToDto(Cart cart);
    Cart cartDtoToEntity(CartDto cartDto);
}


