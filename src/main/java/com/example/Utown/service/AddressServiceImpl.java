package com.example.Utown.service;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.addressDTO.AddressInfoDto;
import com.example.Utown.dto.clientDTO.ClientProfileUpdateDto;
import com.example.Utown.exception.AddressNotFoundException;
import com.example.Utown.exception.DefaultAddressNotSetException;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.AddressMapper;
import com.example.Utown.model.Address;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.repository.AddressRepository;
import com.example.Utown.repository.UserType.ClientRepository;
import com.example.Utown.service.UserType.client.CurrentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final ClientRepository clientRepository;
    private final CurrentService currentService;

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
        return addressRepository.save(address);
    }

    @Override
    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", id));
    }

    @Override //Можно удалить позже
    public List<AddressDto> getAllAddresses() {
        return addressRepository.getAllAddresses();
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

        return addressRepository.save(address);
    }

    @Override //Можно позже удалить
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found", id));
        addressRepository.delete(address);
    }




    @Override
    public List<AddressDto> getAddressesByClient() {
        Client client = currentService.getCurrentClient();
        return clientRepository.getAddressesByClient(client.getUsername());
    }

    @Transactional //For Client
    @Override
    public Address saveAddressForClient(AddressDto dto) {
        Client client = currentService.getCurrentClient();

        Address address = createAddress(dto);

        if (client.getAddresses() == null) {
            client.setAddresses(new HashSet<>());
        }
        client.getAddresses().add(address);

        clientRepository.save(client);

        return address;
    }

    @Transactional
    @Override
    public ClientProfileUpdateDto updateClientProfile(ClientProfileUpdateDto dto) {
        Client client = currentService.getCurrentClient();

        client.setFullName(dto.getFullName());

        Long defaultAddressId = client.getDefaultAddress();
        if (defaultAddressId == null) {
            throw new DefaultAddressNotSetException();
        }

        boolean hasDefaultAddress = client.getAddresses().stream()
                .anyMatch(a -> a.getId().equals(defaultAddressId));

        if (!hasDefaultAddress) {
            throw new ResourceNotFoundException("Default address not found for client", defaultAddressId);
        }

        Address updatedAddress = updateAddress(defaultAddressId, dto.getAddressDto());

        clientRepository.save(client);

        AddressDto updatedAddressDto = addressMapper.addressToDto(updatedAddress);
        return new ClientProfileUpdateDto(client.getFullName(), updatedAddressDto);
    }

    @Override
    public void deleteAddressForCLient(Long addressId) {
        Client client = currentService.getCurrentClient();

        Address address = getAddressById(addressId);

        if (!client.getAddresses().contains(address)) {
            throw new AccessDeniedException("You are not allowed to delete this address");
        }

        client.getAddresses().remove(address);
        clientRepository.save(client);
        addressRepository.delete(address);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Address updateAddressByRestaurant(Long id, AddressInfoDto dto) {
        Address address = getAddressById(id);
        addressRepository.delete(address);
    }

}
