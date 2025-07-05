package com.example.Utown.service;

import com.example.Utown.dto.clientDTO.RestaurantForClientDto;
import com.example.Utown.dto.restaurantAdminDTO.RestaurantAdminCreateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantCreateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.*;
import com.example.Utown.model.OperatingMode;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.Role;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.model.enumFiles.Roles;
import com.example.Utown.repository.OrderRepository;
import com.example.Utown.repository.RestaurantRepository;
import com.example.Utown.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl  implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantInfoMapper restaurantInfoMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestaurantAdminInfoMapper restaurantAdminInfoMapper;
    private final AddressInfoMapper addressInfoMapper;
    private final OperatingModeInfoMapper operatingModeInfoMapper;
    private final FileInfoMapper fileInfoMapper;
    private final RestaurantCategoryInfoMapper restaurantCategoryMapper;
    private final OrderRepository orderRepository;
    private final RestaurantMapper restaurantMapper;

    @Override
    public List<RestaurantInfoDto> getAllRestaurants() {
        return restaurantRepository.findAllRestaurantsWithOrderCount();
    }


    @Override
    public RestaurantDetailsDto getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findRestaurantById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));

        Long orderCounts = orderRepository.countByRestaurantId(id);

       RestaurantDetailsDto dto =  restaurantInfoMapper.toDto(restaurant, orderCounts);
       return dto;

    }

    @Transactional
    @Override
    public RestaurantDetailsDto createRestaurant(RestaurantCreateDto dto) {
        Restaurant restaurant = restaurantInfoMapper.toEntity(dto);

        RestaurantAdminCreateDto adminDto = dto.getRestaurantCreateDto();

        RestaurantAdmin admin = restaurantAdminInfoMapper.toEntity(adminDto);
        admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        Role role = roleRepository.findByName(Roles.valueOf("ROLE_RESTAURANT_ADMIN"))
                .orElseThrow(() -> new ResourceNotFoundException("Role", "ROLE_RESTAURANT_ADMIN"));
        admin.setRoles(Set.of(role));
        admin.setRestaurant(restaurant);
        restaurant.setRestaurantAdmin(admin);

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return restaurantInfoMapper.toDto(savedRestaurant);
    }

    @Transactional
    @Override
    public RestaurantDetailsDto updateRestaurant(Long id, RestaurantUpdateDto dto) {
        // Получаем ресторан из БД или кидаем исключение, если не найден
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));


        restaurantInfoMapper.updateFromDto(dto, restaurant);


        if (dto.getAddress() != null) {
            if (restaurant.getAddress() != null) {
                addressInfoMapper.updateFromDto(dto.getAddress(), restaurant.getAddress());
            } else {
                restaurant.setAddress(addressInfoMapper.toEntity(dto.getAddress()));
            }
        }

        // Обновляем файл, если есть
        if (dto.getFileInfo() != null) {
            fileInfoMapper.updateFromDto(dto.getFileInfo(), restaurant.getFileInfo());
        }

        if (dto.getOperatingModes() != null) {

            List<OperatingMode> updatedOperatingModes = dto.getOperatingModes().stream()
                    .map(operatingModeInfoMapper::toEntity)
                    .collect(Collectors.toList());
            restaurant.setOperatingModes(updatedOperatingModes);
        }


        if (dto.getCategory() != null) {
            restaurantCategoryMapper.updateFromDto(dto.getCategory(), restaurant.getCategory());
        }

        if (dto.getRestaurantAdmin() != null) {
            restaurantAdminInfoMapper.updateFromDto(dto.getRestaurantAdmin(), restaurant.getRestaurantAdmin());
        }


        Restaurant saved = restaurantRepository.save(restaurant);


        return restaurantInfoMapper.toDto(saved);
    }

    @Transactional
    @Override
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));
        restaurantRepository.delete(restaurant);
    }

    @Override //For Client
    public List<RestaurantForClientDto> getAllRestaurantsForClient() {
        List<Restaurant> restaurants = restaurantRepository.getAllActiveRestaurantsForClient();
        return restaurants.stream()
                .map(restaurantMapper::toRestaurantForClientDto)
                .collect(Collectors.toList());
        // добавите сортировку по рейтингу???
    }

    @Override //For Client
    public List<RestaurantForClientDto> getRestaurantsByCategoryId(Long categoryId) {
        List<RestaurantForClientDto> allRestaurants = getAllRestaurantsForClient();

        return allRestaurants.stream()
                .filter(r -> r.getCategoryIds() != null && r.getCategoryIds().contains(categoryId))
                .toList();
    }

    @Override //For Client
    public List<RestaurantForClientDto> getAllRestaurantsSortedByDeliveryTime() {
        List<RestaurantForClientDto> allRestaurants = getAllRestaurantsForClient();

        allRestaurants.sort(Comparator.comparingInt(r -> {
            try {
                return Integer.parseInt(r.getDeliveryTime());
            } catch (Exception e) {
                return Integer.MAX_VALUE;
            }
        }));
        // Добавить везде сортировку по рейтингу???

        return allRestaurants;
    }


}
