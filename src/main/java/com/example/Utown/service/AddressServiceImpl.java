package com.example.Utown.service;


import com.example.Utown.dto.addressDTO.AddressInfoDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.model.Address;
import com.example.Utown.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;;

    @Override
    public List<AddressInfoDto> getAllAddresses() {
        return addressRepository.findAllAddresses();
    }

    @Override
    public AddressInfoDto getAddressById(Long id) {
        return addressRepository.findAddressById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found", id));
    }


    @Override
    public Address createAddress(AddressInfoDto dto) {
        Address address = new Address();
        address.setArea(dto.getArea());
        address.setCity(dto.getCity());
        address.setDetails(dto.getDetails());
        address.setFullAddress(dto.getFullAddress());
        address.setLatitude(dto.getLatitude());
        address.setLongitude(dto.getLongitude());
        address.setPostCode(dto.getPostCode());
        address.setState(dto.getState());
        address.setStreet(dto.getStreet());
        address.setIntercomCode(dto.getIntercomCode());
        address.setTypeAddress(dto.getTypeAddress());
        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Long id, AddressInfoDto dto) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", id));

        address.setArea(dto.getArea());
        address.setCity(dto.getCity());
        address.setDetails(dto.getDetails());
        address.setFullAddress(dto.getFullAddress());
        address.setLatitude(dto.getLatitude());
        address.setLongitude(dto.getLongitude());
        address.setPostCode(dto.getPostCode());
        address.setState(dto.getState());
        address.setStreet(dto.getStreet());
        address.setIntercomCode(dto.getIntercomCode());
        address.setTypeAddress(dto.getTypeAddress());

        return addressRepository.save(address);
    }

    @Override
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found", id));
        addressRepository.delete(address);
    }

}