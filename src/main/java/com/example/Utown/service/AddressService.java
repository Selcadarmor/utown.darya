package com.example.Utown.service;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.addressDTO.AddressInfoDto;
import com.example.Utown.dto.clientDTO.ClientProfileUpdateDto;
import com.example.Utown.model.Address;

import java.util.List;

public interface AddressService {
    Address createAddress(AddressDto dto);
    Address getAddressById(Long id);
    List<AddressDto> getAllAddresses();
    Address updateAddress(Long id, AddressDto dto);
    void deleteAddress(Long id);
    List<AddressDto> getAddressesByClient();
    Address saveAddressForClient(AddressDto dto);
    ClientProfileUpdateDto updateClientProfile(ClientProfileUpdateDto dto);
    Address updateAddressByRestaurant(Long id, AddressInfoDto dto);
    void deleteAddressForCLient(Long addressId);
}
