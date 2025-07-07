package com.example.Utown.service;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.model.Address;
import com.example.Utown.repository.AddressRepository;
import com.example.Utown.repository.UserType.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final ClientRepository clientRepository;

    @Override
    public Address createAddress(AddressDto dto) {
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
        return address;
    }

    @Override
    public AddressDto getAddressById(Long id) {
        return addressRepository.findAddressById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found", id));
    }

    @Override
    public List<AddressDto> getAllAddresses() {
        return addressRepository.findAllAddresses();
    }

    @Override
    public Address updateAddress(Long id, AddressDto dto) {
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
