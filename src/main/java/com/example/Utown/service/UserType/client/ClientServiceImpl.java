package com.example.Utown.service.UserType.client;

import com.example.Utown.dto.clientDto.ClientInfoDto;
import com.example.Utown.dto.clientDto.ClientUpdateDto;
import com.example.Utown.dto.userDto.UserChangePasswordDto;
import com.example.Utown.dto.userDto.UserProfileUpdateDto;
import com.example.Utown.exception.*;
import com.example.Utown.model.Address;
import com.example.Utown.model.User;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.repository.AddressRepository;
import com.example.Utown.repository.UserRepository;
import com.example.Utown.repository.UserType.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
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
                                client.getAddresses(),
                                client.getOrders() != null ? client.getOrders().size() : 0,
                                null
                        ))
                .toList();
    }


    @Override //For Admin + Client
    public ClientInfoDto getClientById(Long clientId) {
        Client client =  clientRepository.findAllClientInfoById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", clientId));
        return  new ClientInfoDto(
                client.getId(),
                client.getFullName(),
                client.getUsername(),
                client.getAddresses(),
                client.getOrders() != null ? client.getOrders().size() : 0,
                client.getFileInfo()
        );
    }

    @Transactional //For Admin
    @Override
    public ClientInfoDto updateClient(Long id, ClientUpdateDto clientDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
        client.setActive(clientDto.isActive());
        clientRepository.save(client);

        return new ClientInfoDto(
                client.getId(),
                client.getFullName(),
                client.getUsername(),
                client.getAddresses(),
                client.getOrders() != null ? client.getOrders().size() : 0,
                client.getFileInfo()
        );
    }

    @Transactional // For Client
    public void updateProfile(String currentUsername, UserProfileUpdateDto dto) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserNotFoundException(currentUsername));

        if (!user.getUsername().equals(dto.getUsername())
                && userRepository.existsByUsername(dto.getUsername())) {
            throw new UserAlreadyExistsException(dto.getUsername());
        }

        user.setUsername(dto.getUsername());

        if (user instanceof Client client) {
            client.setFullName(dto.getFullName());

            Long addressId = client.getDefaultAddress();
            if (addressId != null && dto.getFullAddress() != null) {
                Address address = addressRepository.findById(addressId)
                        .orElseThrow(() -> new AddressNotFoundException(addressId));
                address.setFullAddress(dto.getFullAddress());
            }
        }
    }

    @Override //For Client
    public void changePassword(String username, UserChangePasswordDto dto) {
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


}

