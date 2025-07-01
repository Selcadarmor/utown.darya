package com.example.Utown.ADDRESSCHECK;

import java.util.List;

public interface AddressService {
    Address createAddress(AddressDto dto);
    AddressDto getAddressById(Long id);
    List<AddressDto> getAllAddresses();
    Address updateAddress(Long id, AddressDto dto);
    void deleteAddress(Long id);

}
