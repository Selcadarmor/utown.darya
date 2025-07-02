package com.example.Utown.service;


import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.model.Address;

import java.util.List;
import java.util.Set;

public interface AddressService {
    Address createAddress(AddressDto dto);
    AddressDto getAddressById(Long id);
    List<AddressDto> getAllAddresses();
    Address updateAddress(Long id, AddressDto dto);
    void deleteAddress(Long id);

}
