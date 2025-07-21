package com.example.Utown.service.UserType.client;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.addressDTO.AddressInfoDto;
import com.example.Utown.dto.clientDTO.ClientDetailsDto;
import com.example.Utown.dto.clientDTO.ClientInfoDto;
import com.example.Utown.dto.clientDTO.ClientProfileUpdateDto;
import com.example.Utown.dto.clientDTO.ClientRegistrationDto;
import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
import com.example.Utown.exception.DefaultAddressNotSetException;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.exception.RestaurantAlreadyFavoritedException;
import com.example.Utown.exception.RestaurantNotFoundException;
import com.example.Utown.exception.RestaurantNotInFavoritesException;
import com.example.Utown.exception.UserAlreadyExistsException;
import com.example.Utown.mapper.AddressMapper;
import com.example.Utown.model.Address;
import com.example.Utown.model.Cart;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.Role;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.model.enumFiles.Roles;
import com.example.Utown.repository.AddressRepository;
import com.example.Utown.repository.CartRepository;
import com.example.Utown.repository.RestaurantRepository;
import com.example.Utown.repository.UserType.ClientRepository;
import com.example.Utown.service.AddressService;
import com.example.Utown.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final AddressService addressService;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final ClientRepository clientRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final RestaurantRepository restaurantRepository;

    // ========================= GET =========================

    @Override
    public Client findByUsername(String username) {
        return clientRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public Page<ClientDetailsDto> getAllClients(Pageable pageable) {
        return clientRepository.findAllClientDetails(pageable);
    }


    @Override
    public ClientInfoDto getClientById(Long clientId) {
        return clientRepository.findClientInfoById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found", clientId));
    }

    @Override
    public List<AddressDto> getAddressesByClient() {
        Client client = getCurrentClient();
        return clientRepository.getAddressesByClient(client.getUsername());
    }

    @Transactional(readOnly = true)
    public List<RestaurantForClientDto> getFavoriteRestaurants() {
        Client client = getCurrentClient();
        return clientRepository.findFavoriteRestaurants(client.getUsername());
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

    // ========================= POST =========================

    @Override // For Client
    public void save(ClientRegistrationDto dto, Roles roleName) {

        if (clientRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(dto.getUsername());
        }

        Role role = roleService.findByName(roleName);

        Cart cart = new Cart(); // создаём пустую корзину
        cartRepository.save(cart);

        Client client = new Client();
        client.setUsername(dto.getUsername());
        client.setPassword(passwordEncoder.encode(dto.getPassword()));
        client.setRoles(Set.of(role));
        client.setActive(true);
        client.setFullName(null);
        client.setDefaultAddress(null);
        client.setCart(cart);
        client.setFavoriteRestaurants(new HashSet<>());
        client.setAddresses(new HashSet<>());
        client.setOrders(new ArrayList<>());
        client.setNotifications(new HashSet<>());
        client.setFileInfo(null);

        clientRepository.save(client);
    }

    @Transactional //For Client
    @Override
    public Address saveAddressForClient(AddressDto dto) {
        Client client = getCurrentClient();

        Address address = addressService.createAddress(dto);

        if (client.getAddresses() == null) {
            client.setAddresses(new HashSet<>());
        }
        client.getAddresses().add(address);
        clientRepository.save(client);

        return address;
    }

    @Transactional //For client
    @Override
    public void addFavoriteRestaurant(Long restaurantId) {
        Client client = getCurrentClient();

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));

        if (client.getFavoriteRestaurants().contains(restaurant)) {
            throw new RestaurantAlreadyFavoritedException(restaurant.getTitle());
        }

        client.getFavoriteRestaurants().add(restaurant);
        clientRepository.save(client);
    }

    // ========================= PUT =========================

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void updateClientActiveStatus(Long id, Boolean active) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
        client.setActive(active);
        clientRepository.save(client);
    }

    @Transactional
    @Override
    public ClientProfileUpdateDto updateClientProfile(ClientProfileUpdateDto dto) {
        Client client = getCurrentClient();

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

        Address updatedAddress = addressService.updateAddress(defaultAddressId, dto.getAddressDto());

        clientRepository.save(client);

        AddressDto updatedAddressDto = addressMapper.addressToDto(updatedAddress);
        return new ClientProfileUpdateDto(client.getFullName(), updatedAddressDto);
    }


    // ========================= DELETE =========================

    @Override
    public void deleteAddressForCLient(Long addressId) {
        Client client = getCurrentClient();

        Address address = addressService.getAddressById(addressId);

        if (!client.getAddresses().contains(address)) {
            throw new AccessDeniedException("You are not allowed to delete this address");
        }

        client.getAddresses().remove(address);
        clientRepository.save(client);
        addressRepository.delete(address);
    }


    @Transactional // For Client
    @Override
    public void removeFavoriteRestaurant(Long restaurantId) {
        Client client = getCurrentClient();
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));

        if (!client.getFavoriteRestaurants().contains(restaurant)) {
            throw new RestaurantNotInFavoritesException(restaurant.getTitle());
        }

        client.getFavoriteRestaurants().remove(restaurant);
        clientRepository.save(client);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteClient(Long id) {
        updateClientActiveStatus(id, false);
    }

    // ========================= PRIVATE =========================


    private Set<AddressInfoDto> mapAddressDtos(Set<Address> addresses) {
        if (addresses == null) return Collections.emptySet();
        return addresses.stream()
                .map(address -> new AddressInfoDto(
                        address.getId(),
                        address.getCity(),
                        address.getFullAddress()
                ))
                .collect(Collectors.toSet());
    }
}
