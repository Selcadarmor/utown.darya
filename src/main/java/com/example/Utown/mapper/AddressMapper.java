package com.example.Utown.mapper;

import com.example.Utown.dto.AddressDto;
import com.example.Utown.dto.RestaurantDto;
import com.example.Utown.model.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDto toDto(Address address);
    Address toAddress(AddressDto dto);
}
