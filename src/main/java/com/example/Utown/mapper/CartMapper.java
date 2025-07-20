package com.example.Utown.mapper;

import com.example.Utown.dto.cartDTO.CartDto;
import com.example.Utown.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {DishToOrderMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartMapper {

    @Mapping(target = "dishToOrders", ignore = true)
    CartDto cartToDto(Cart cart);


    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Cart cartDtoToEntity(CartDto cartDto);
}
