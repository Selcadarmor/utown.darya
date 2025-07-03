package com.example.Utown.service;

import com.example.Utown.dto.addressDTO.AddressInfoDto;
import com.example.Utown.model.Address;

import java.util.List;


public interface AddressService {
    Address createAddress(AddressInfoDto dto);
    AddressInfoDto getAddressById(Long id);
    List<AddressInfoDto> getAllAddresses();
    Address updateAddress(Long id, AddressInfoDto dto);
    void deleteAddress(Long id);

}