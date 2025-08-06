package com.example.Utown.service;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.model.Address;
import com.example.Utown.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    @Transactional
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

        Address saved = addressRepository.save(address);
        log.info("Created new address with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> {
                    return new ResourceNotFoundException("Address", id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Address updateAddress(Long id, AddressDto dto) {
        Address address = getAddressById(id);

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

        Address updated = addressRepository.save(address);
        log.info("Updated address with id: {}", id);
        return updated;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteAddress(Long id) {
        Address address = getAddressById(id);
        addressRepository.delete(address);
        log.info("Deleted address with id: {}", id);
    }
}
