package com.example.Utown.mapper;

import com.example.Utown.dto.cartDTO.CartDto;
import com.example.Utown.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "user.id", target = "userId")
    CartDto cartToDto(Cart cart);

    @Mapping(source = "userId", target = "user.id")
    Cart cartDtoToEntity(CartDto dto);
}

