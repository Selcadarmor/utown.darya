package com.example.Utown.service;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.model.Address;

import java.util.List;

public interface AddressService {
    Address getAddressById(Long id);
    List<Address> getAllAddresses();
    Address createAddress(AddressDto dto);
    Address updateAddress(Long id, AddressDto dto);
    void deleteAddress(Long id);
}
