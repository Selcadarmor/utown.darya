package com.example.Utown.service.UserType.client;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.clientDTO.ClientChangePasswordDto;
import com.example.Utown.dto.clientDTO.ClientInfoDto;
import com.example.Utown.dto.clientDTO.ClientProfileUpdateDto;
import com.example.Utown.dto.clientDTO.ClientUpdateDto;
import com.example.Utown.exception.PasswordsDoNotMatchException;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.exception.UserNotFoundException;
import com.example.Utown.model.Address;
import com.example.Utown.model.User;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.repository.AddressRepository;
import com.example.Utown.repository.UserRepository;
import com.example.Utown.repository.UserType.ClientRepository;
import com.example.Utown.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final AddressService addressService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<Client> findByUsername(String username) {
        return clientRepository.findByUsername(username);
    }


    @Override // For Admin
    public List<ClientInfoDto> getAllClients() {
        List<Client> clients = clientRepository.findAllWithAddressesAndOrders();
        return  clients.stream()
                        .map(client -> new ClientInfoDto(
                                client.getId(),
                                client.getFullName(),
                                client.getUsername(),
                                client.getAddresses() != null ? extractAddressIds(client.getAddresses()) : null,
                                client.getOrders() != null ? client.getOrders().size() : 0,
                                null
                        ))
                .toList();
    }


    @Override //For Admin + Client
    public ClientInfoDto getClientById(Long clientId) {
        Client client =  clientRepository.findAllClientInfoById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", clientId));
        Set<Long> addressIds = extractAddressIds(client.getAddresses());

        return  new ClientInfoDto(
                client.getId(),
                client.getFullName(),
                client.getUsername(),
                addressIds,
                client.getOrders() != null ? client.getOrders().size() : 0,
                client.getFileInfo() != null ? client.getFileInfo().getId() : null
        );
    }

    @Transactional //For Admin
    @Override
    public ClientInfoDto updateClient(Long id, ClientUpdateDto clientDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
        client.setActive(clientDto.isActive());
        clientRepository.save(client);

        Set<Long> addressIds = extractAddressIds(client.getAddresses());

        return new ClientInfoDto(
                client.getId(),
                client.getFullName(),
                client.getUsername(),
                addressIds,
                client.getOrders() != null ? client.getOrders().size() : 0,
                client.getFileInfo().getId()
        );
    }

    @Transactional //For Client
    @Override
    public void saveAddressForClient(Long clientId, AddressDto dto) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found", clientId));

        Address address = addressService.createAddress(dto);

        client.getAddresses().add(address);
        clientRepository.save(client);
    }

    @Transactional //For Client
    @Override
    public void updateClientProfile(Long clientId, ClientProfileUpdateDto dto) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found", clientId));

        client.setFullName(dto.getFullName());

        Set<Address> updatedAddresses = new HashSet<>();
        for (AddressDto addressDto : dto.getAddresses()) {
            Address updatedAddress = addressService.updateAddress(addressDto.getId(), addressDto);
            updatedAddresses.add(updatedAddress);
        }

        client.setAddresses(updatedAddresses);
        clientRepository.save(client);
    }

    @Override //For Client
    public void changePassword(String username, ClientChangePasswordDto dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }


    @Transactional //For Admin + Client
    @Override
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client", id);
        }
        clientRepository.deleteById(id);
    }

    private Set<Long> extractAddressIds(Set<Address> addresses) {
        return addresses.stream()
                .map(Address::getId)
                .collect(Collectors.toSet());
    }
    @Override
    public Client getCurrentClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        String username = authentication.getName();
        return clientRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Client not found: " + username));
    }

    @Override
    public Address getAddressByDefaultAddress() {
        Client client = getCurrentClient();

        Long defaultAddressId = client.getDefaultAddress();
        if (defaultAddressId == null) {
            throw new IllegalStateException("Default address is not set for client");
        }

        return addressService.getAddressById(defaultAddressId);
    }

}

