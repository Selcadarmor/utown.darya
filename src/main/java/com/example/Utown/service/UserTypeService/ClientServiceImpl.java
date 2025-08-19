package com.example.Utown.service.UserTypeService;

import com.example.Utown.config.S3.AwsProperties;
import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.clientDTO.ClientDetailsDto;
import com.example.Utown.dto.clientDTO.ClientInfoDto;
import com.example.Utown.dto.clientDTO.ClientProfileUpdateDto;
import com.example.Utown.dto.clientDTO.ClientRegistrationDto;
import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
import com.example.Utown.exception.DefaultAddressNotSetException;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.exception.RestaurantAlreadyFavoritedException;
import com.example.Utown.exception.RestaurantNotInFavoritesException;
import com.example.Utown.exception.UserNotFoundException;
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
import com.example.Utown.service.CartService;
import com.example.Utown.service.S3Service.FileInfoService;
import com.example.Utown.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final AddressService addressService;
    private final AddressMapper addressMapper;
    private final CartService cartService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final RestaurantRepository restaurantRepository;
    private final FileInfoService fileInfoService;
    private final AwsProperties awsProperties;

    // ========================= GET =========================

    @Override
    public Client findByUsername(String username) {
        return clientRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Client not found with username: {}", username);
                    return new UsernameNotFoundException(username);
                });
    }

    @Override
    public Client getById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Client not found with id: {}", id);
                    return new ResourceNotFoundException("Client", id);
                });
    }

    @Override
    public ClientInfoDto getClientById(Long clientId) {
        ClientInfoDto dto = clientRepository.findClientInfoById(clientId)
                .orElseThrow(() -> {
                    log.warn("Client not found with id: {}", clientId);
                    return new ResourceNotFoundException("Client", clientId);
                });

        if (dto.getPath() != null && !dto.getPath().isEmpty()) {
            String url = awsProperties.getPublicBaseUrl() + "/" + dto.getPath();
            dto.setFileUrl(url);
        }

        log.info("Client info retrieved for id: {}", clientId);
        return dto;
    }


    @Override
    public Page<ClientDetailsDto> getAllClients(String query, Boolean isActive, Pageable pageable) {
        return clientRepository.findAllClientDetails(query, isActive, pageable);
    }

    @Override
    public List<AddressDto> getAddressesByClient() {
        Client client = getCurrentClient();
        log.info("Retrieving addresses for client: {}", client.getUsername());
        return clientRepository.getAddressesByClient(client.getUsername());
    }


    @Override
    public Client getCurrentClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Unauthenticated access attempt to getCurrentClient");
            throw new RuntimeException("User is not authenticated");
        }

        String username = authentication.getName();
        return clientRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Client not found with username: {}", username);
                    return new UsernameNotFoundException("Client not found: " + username);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantForClientDto> getFavoriteRestaurants() {
        Client client = getCurrentClient();
        log.info("Fetching favorite restaurants for client: {}", client.getUsername());

        return clientRepository.findFavoriteRestaurants(client.getUsername());
    }

    // ========================= POST =========================

    @Override // For Client
    @Transactional
    public void save(ClientRegistrationDto dto, Roles roleName) {

        Role role = roleService.findByName(roleName);

        Cart cart = cartService.createCart();

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

        log.info("Registering new client with username: {}", dto.getUsername());

        clientRepository.save(client);
    }

    @Override
    @Transactional
    public Address saveAddressForClient(AddressDto dto) {
        Client client = getCurrentClient();

        Address address = addressService.createAddress(dto);

        if (client.getAddresses() == null) {
            client.setAddresses(new HashSet<>());
        }

        client.getAddresses().add(address);

        if (client.getDefaultAddress() == null) {
            client.setDefaultAddress(address.getId());
        }

        log.info("Saving new address for client: {}. Default address: {}", client.getUsername(), client.getDefaultAddress());

        clientRepository.save(client);

        return address;
    }

    @Override //For client
    @Transactional
    public void addFavoriteRestaurant(Long restaurantId) {
        Client client = getCurrentClient();

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", restaurantId));

        if (client.getFavoriteRestaurants().contains(restaurant)) {
            log.warn("Client '{}' already added restaurant '{}' to favorites", client.getUsername(), restaurant.getTitle());
            throw new RestaurantAlreadyFavoritedException(restaurant.getTitle());
        }

        client.getFavoriteRestaurants().add(restaurant);
        clientRepository.save(client);

        log.info("Client '{}' added restaurant '{}' to favorites", client.getUsername(), restaurant.getTitle());
    }


    // ========================= PUT =========================

    @Override
    @Transactional
    public ClientProfileUpdateDto updateClientProfile(ClientProfileUpdateDto dto) {
        Client client = getCurrentClient();

        client.setFullName(dto.getFullName());

        if (dto.getFileId() != null) {
            FileInfo fileInfo = fileInfoService.findById(dto.getFileId());
            client.setFileInfo(fileInfo);
        } else {
            client.setFileInfo(null);
        }

        Long defaultAddressId = client.getDefaultAddress();
        if (defaultAddressId == null) {
            log.warn("Default address is not set for client: {}", client.getUsername());
            throw new DefaultAddressNotSetException();
        }

        boolean hasDefaultAddress = client.getAddresses().stream()
                .anyMatch(a -> a.getId().equals(defaultAddressId));
        if (!hasDefaultAddress) {
            log.error("Default address ID {} not found among client's addresses: {}", defaultAddressId, client.getUsername());
            throw new ResourceNotFoundException("Default address not found for client", defaultAddressId);
        }

        Address updatedAddress = addressService.updateAddress(defaultAddressId, dto.getAddressDto());

        clientRepository.save(client);

        log.info("Client profile updated successfully: {}", client.getUsername());

        AddressDto updatedAddressDto = addressMapper.addressToDto(updatedAddress);

        Long fileId = client.getFileInfo() != null ? client.getFileInfo().getId() : null;

        return new ClientProfileUpdateDto(client.getFullName(), updatedAddressDto, fileId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateClientActiveStatus(Long id, Boolean active) {
        Client client = getById(id);
        client.setIsActive(active);
        clientRepository.save(client);
        log.info("Successfully updated active status for client ID {} to: {}", id, active);
    }

    // ========================= DELETE =========================

    @Override // For Client
    @Transactional
    public void removeFavoriteRestaurant(Long restaurantId) {
        Client client = getCurrentClient();

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> {
                    log.warn("Attempted to remove favorite restaurant but not found, id: {}", restaurantId);
                    return new ResourceNotFoundException("Restaurant", restaurantId);
                });

        Set<Restaurant> favorites = client.getFavoriteRestaurants();

        if (favorites == null || !favorites.contains(restaurant)) {
            log.warn("Attempted to remove restaurant '{}' which is not in favorites", restaurant.getTitle());
            throw new RestaurantNotInFavoritesException(restaurant.getTitle());
        }

        favorites.remove(restaurant);
        clientRepository.save(client);
        log.info("Removed restaurant '{}' from favorites of client '{}'", restaurant.getTitle(), client.getUsername());
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void deleteClient(Long id) {
        updateClientActiveStatus(id, false);
    }

}
