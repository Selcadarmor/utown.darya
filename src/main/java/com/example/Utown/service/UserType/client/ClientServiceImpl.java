package com.example.Utown.service.UserType.client;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.adminDto.ClientInfoDto;
import com.example.Utown.dto.adminDto.ClientUpdateDto;
import com.example.Utown.dto.adminDto.OrderShortDto;
import com.example.Utown.dto.clientDto.ClientChangePasswordDto;
import com.example.Utown.exception.*;
import com.example.Utown.mapper.AddressMapper;
import com.example.Utown.model.Address;
import com.example.Utown.model.User;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.repository.AddressRepository;
import com.example.Utown.repository.OrderRepository;
import com.example.Utown.repository.UserRepository;
import com.example.Utown.repository.UserType.ClientRepository;
import com.example.Utown.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressService addressService;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressMapper addressMapper;

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

    @Transactional
    public Client updateClientProfile(Long clientId, String newFullName, List<AddressDto> updatedAddresses) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found", clientId));

        client.setFullName(newFullName);

        if (updatedAddresses != null && !updatedAddresses.isEmpty()) {
            Set<Address> clientAddresses = client.getAddresses();

            for (AddressDto dto : updatedAddresses) {
                if (dto.getId() == null) {
                    throw new IllegalArgumentException("Address ID must be provided for update");
                }

                Address addressToUpdate = clientAddresses.stream()
                        .filter(addr -> addr.getId().equals(dto.getId()))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Address not found in client addresses", dto.getId()));

                addressService.updateAddress(addressToUpdate.getId(), dto);
            }
        }

        return clientRepository.save(client);
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

