package com.example.Utown.service.UserTypeService;

import com.example.Utown.dto.addressDTO.AddressDto;
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
import com.example.Utown.mapper.AddressMapper;
import com.example.Utown.model.Address;
import com.example.Utown.model.Cart;
import com.example.Utown.model.FileInfo;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.Role;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.model.enumFiles.Roles;
import com.example.Utown.repository.RestaurantRepository;
import com.example.Utown.repository.UserType.ClientRepository;
import com.example.Utown.service.AddressService;
import com.example.Utown.service.S3Service.FileInfoService;
import com.example.Utown.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final AddressService addressService;
    private final AddressMapper addressMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final RestaurantRepository restaurantRepository;
    private final FileInfoService fileInfoService;

    // ========================= GET =========================

    @Override
    public Client findByUsername(String username) {
        return clientRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public ClientInfoDto getClientById(Long clientId) {
        return clientRepository.findClientInfoById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found", clientId));
    }

    @Override
    public Page<ClientDetailsDto> getAllClients(String query, Boolean isActive, Pageable pageable) {
        return clientRepository.findAllClientDetails(query, isActive, pageable);
    }

    @Override //For Client
    public List<AddressDto> getAddressesByClient() {
        Client client = getCurrentClient();
        return clientRepository.getAddressesByClient(client.getUsername());
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

    @Transactional(readOnly = true)
    public List<RestaurantForClientDto> getFavoriteRestaurants() {
        Client client = getCurrentClient();
        return clientRepository.findFavoriteRestaurants(client.getUsername());
    }

    // ========================= POST =========================

    @Override // For Client
    public void save(ClientRegistrationDto dto, Roles roleName) {

        Role role = roleService.findByName(roleName);

        Cart cart = new Cart();

        Client client = new Client();
        client.setUsername(dto.getUsername());
        client.setPassword(passwordEncoder.encode(dto.getPassword()));
        client.setRoles(Set.of(role));
        client.setIsActive(true);
        client.setFullName(null);
        client.setDefaultAddress(null);
        client.setFavoriteRestaurants(new HashSet<>());
        client.setAddresses(new HashSet<>());
        client.setOrders(new ArrayList<>());
        client.setNotifications(new HashSet<>());
        client.setFileInfo(null);

        client.setCart(cart);
        cart.setClient(client);

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

    @Transactional
    @Override
    public ClientProfileUpdateDto updateClientProfile(ClientProfileUpdateDto dto) {
        Client client = getCurrentClient();

        client.setFullName(dto.getFullName());

        if (dto.getFileId() != null) {
            FileInfo fileInfo = fileInfoService.findById(dto.getFileId());
            client.setFileInfo(fileInfo);
        }

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
        return new ClientProfileUpdateDto(client.getFullName(), updatedAddressDto, client.getFileInfo().getId());
    }


    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void updateClientActiveStatus(Long id, Boolean active) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
        client.setIsActive(active);
        clientRepository.save(client);
    }

    // ========================= DELETE =========================

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


    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void deleteClient(Long id) {
        updateClientActiveStatus(id, false);
    }

}
