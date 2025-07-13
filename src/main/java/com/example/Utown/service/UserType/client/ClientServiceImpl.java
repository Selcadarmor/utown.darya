package com.example.Utown.service.UserType.client;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.clientDTO.ClientInfoDto;
import com.example.Utown.dto.clientDTO.ClientProfileUpdateDto;
import com.example.Utown.dto.clientDTO.ClientRegistrationDto;
import com.example.Utown.dto.clientDTO.ClientUpdateDto;
import com.example.Utown.exception.PasswordsDoNotMatchException;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.exception.RoleNotFoundException;
import com.example.Utown.exception.UserAlreadyExistsException;
import com.example.Utown.model.Address;
import com.example.Utown.model.Cart;
import com.example.Utown.model.Role;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.model.enumFiles.Roles;
import com.example.Utown.repository.AddressRepository;
import com.example.Utown.repository.CartRepository;
import com.example.Utown.repository.RoleRepository;
import com.example.Utown.repository.UserType.ClientRepository;
import com.example.Utown.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final RoleRepository roleRepository;
    private final AddressService addressService;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

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


    @Override //For Admin //сделан
    public ClientInfoDto getClientById(Long clientId) {
        Client client =  findClientByIdOrThrow(clientId);
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

    @Transactional(rollbackFor = Exception.class) //For Admin сделано
    @Override
    public void updateClient(Long id, ClientUpdateDto clientDto) {
        Client client = findClientByIdOrThrow(id);
        client.setActive(clientDto.isActive());
        clientRepository.save(client);
    }

    @Override // For client
    public void save(ClientRegistrationDto dto, Roles roleName) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        if (clientRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(dto.getUsername());
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName.name()));

        Cart cart = new Cart(); // создаём пустую корзину
        cartRepository.save(cart);

        Client client = new Client();
        client.setUsername(dto.getUsername());
        client.setPassword(passwordEncoder.encode(dto.getPassword()));
        client.setRoles(Set.of(role));
        client.setActive(true);
        client.setCart(cart);
        clientRepository.save(client);
    }

    @Transactional //For Client
    @Override
    public void saveAddressForClient(String username, AddressDto dto) {
        Client client = clientRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found", username));

        Address address = addressService.createAddress(dto);

        client.getAddresses().add(address);
        clientRepository.save(client);
    }//можно переиспользовать тот код что ниже приватный

    @Transactional //For Client
    @Override
    public void updateClientProfile(String username, ClientProfileUpdateDto dto) {
        Client client = clientRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Client", username));

        client.setFullName(dto.getFullName());

        for (AddressDto addressDto : dto.getAddresses()) {
            Address address = client.getAddresses().stream()
                    .filter(a -> a.getId().equals(addressDto.getId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Address", addressDto.getId()));

            addressService.updateAddress(address.getId(), addressDto);
        }

        clientRepository.save(client);
    }// если нужно можете переиспользовать приватный метод

    @Override //For Client
    public void deleteAddressForCLient(Long addressId, String username) {
        Client client = clientRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Client", username));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", addressId));

        if (!client.getAddresses().contains(address)) {
            throw new AccessDeniedException("You are not allowed to delete this address");
        }

        client.getAddresses().remove(address);
        clientRepository.save(client);
        addressRepository.delete(address);
    }


    @Transactional(rollbackFor = Exception.class) //For Admin + Client
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

    private Client findClientByIdOrThrow(Long clientId) { //метод для переиспользования
        return clientRepository.findAllClientInfoById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", clientId));
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

