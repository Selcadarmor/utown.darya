package com.example.Utown.mapper;


import com.example.Utown.dto.addressDTO.AddressInfoDto;
import com.example.Utown.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressInfoMapper {
    AddressInfoDto addressToDto(Address address);
    Address toEntity(AddressInfoDto addressInfoDto);
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Address updateFromDto(AddressInfoDto addressInfoDto, @MappingTarget Address address);
}