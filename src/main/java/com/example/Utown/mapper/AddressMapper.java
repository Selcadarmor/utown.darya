package com.example.Utown.mapper;


import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.model.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDto addressToDto(Address address);
}

