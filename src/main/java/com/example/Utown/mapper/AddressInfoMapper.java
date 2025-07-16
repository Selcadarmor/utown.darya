package com.example.Utown.mapper;


import com.example.Utown.dto.addressDTO.AddressInfoDto;
import com.example.Utown.model.Address;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressInfoMapper {
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "area", ignore = true)
    @Mapping(target = "street", ignore = true)
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "latitude", ignore = true)
    @Mapping(target = "longitude", ignore = true)
    @Mapping(target = "postCode", ignore = true)
    @Mapping(target = "intercomCode", ignore = true)
    @Mapping(target = "typeAddress", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Address toEntity(AddressInfoDto addressInfoDto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "area", ignore = true)
    @Mapping(target = "street", ignore = true)
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "latitude", ignore = true)
    @Mapping(target = "longitude", ignore = true)
    @Mapping(target = "postCode", ignore = true)
    @Mapping(target = "intercomCode", ignore = true)
    @Mapping(target = "typeAddress", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Address updateFromDto(AddressInfoDto addressInfoDto, @MappingTarget Address address);


    AddressInfoDto toDto(Address address);

}