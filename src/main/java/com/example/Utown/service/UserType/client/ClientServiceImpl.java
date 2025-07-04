package com.example.Utown.service.UserType.client;

import com.example.Utown.dto.adminDto.ClientInfoDto;
import com.example.Utown.dto.adminDto.ClientUpdateDto;
import com.example.Utown.dto.adminDto.OrderShortDto;
import com.example.Utown.dto.clientDto.ClientChangePasswordDto;
import com.example.Utown.dto.clientDto.ClientProfileUpdateDto;
import com.example.Utown.exception.*;
import com.example.Utown.model.Address;
import com.example.Utown.model.User;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.repository.AddressRepository;
import com.example.Utown.repository.OrderRepository;
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
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<Client> findByUsername(String username) {
        return clientRepository.findByUsername(username);
    }


    @Override //For Admin
    public List<ClientInfoDto> getAllClients() {
        return clientRepository.findAllClientInfos();
    }

    @Override //For Admin + Client
    public ClientInfoDto getClientById(Long id) {
        return clientRepository.findAllClientInfoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    @Transactional //For Admin
    @Override
    public ClientInfoDto updateClient(Long id, ClientUpdateDto clientDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
        client.setFullName(clientDto.getFullName());
        client.setUsername(clientDto.getUsername());
        client.setActive(clientDto.isActive());

        clientRepository.save(client);

        return clientRepository.findAllClientInfoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    @Transactional // For Client
    public void updateProfile(String currentUsername, ClientProfileUpdateDto dto) {
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

    @Override
    public List<OrderShortDto> getClientOrders(Long clientId) {
        return orderRepository.findAllOrdersByClientId(clientId);
    }

}

