package com.example.Utown.service;

import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
import com.example.Utown.dto.restaurantAdminDTO.RestaurantAdminCreateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantCreateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.*;
import com.example.Utown.model.*;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.model.enumFiles.Roles;
import com.example.Utown.repository.OrderRepository;
import com.example.Utown.repository.RestaurantCategoryRepository;
import com.example.Utown.repository.RestaurantRepository;
import com.example.Utown.repository.RoleRepository;
import com.example.Utown.service.UserType.client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final AddressInfoMapper addressInfoMapper;
    private final AddressService addressService;
    private final ClientService clientService;
    private final FileInfoMapper fileInfoMapper;
    private final OperatingModeInfoMapper operatingModeInfoMapper;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestaurantAdminInfoMapper restaurantAdminInfoMapper;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final RestaurantInfoMapper restaurantInfoMapper;
    private final RestaurantRepository restaurantRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<RestaurantInfoDto> getAllRestaurants() {
        return restaurantRepository.findAllRestaurantsWithOrderCount();
    }


    @Override
    public RestaurantDetailsDto getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findRestaurantById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));

        Long orderCounts = orderRepository.countByRestaurantId(id);

        RestaurantDetailsDto dto = restaurantInfoMapper.toDto(restaurant, orderCounts);
        return dto;

    }

    @Transactional
    @Override
    public RestaurantDetailsDto createRestaurant(RestaurantCreateDto dto) {
        Restaurant restaurant = restaurantInfoMapper.toEntity(dto);

        // 👤 Admin
        RestaurantAdminCreateDto adminDto = dto.getRestaurantAdmin(); // выбери одно поле!
        RestaurantAdmin admin = restaurantAdminInfoMapper.toEntity(adminDto);
        admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        Role role = roleRepository.findByName(Roles.ROLE_RESTAURANT_ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "ROLE_RESTAURANT_ADMIN"));

        admin.setRoles(Set.of(role));
        admin.setRestaurant(restaurant);
        restaurant.setRestaurantAdmin(admin);

        // 📁 File
        if (dto.getFileInfo() != null) {
            restaurant.setFileInfo(fileInfoMapper.toDetailsEntity(dto.getFileInfo()));
        }

        // 🏷 Categories
        if (dto.getCategories() != null && !dto.getCategories().isEmpty()) {
            Set<RestaurantCategory> categories = dto.getCategories().stream()
                    .map(id -> restaurantCategoryRepository.findById(id.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Category", id)))
                    .collect(Collectors.toSet());
            restaurant.setCategories(categories);
        }

        // 🕒 Operating Modes
        if (dto.getOperatingModes() != null && !dto.getOperatingModes().isEmpty()) {
            List<OperatingMode> modes = dto.getOperatingModes().stream()
                    .map(operatingModeInfoMapper::toEntity)
                    .collect(Collectors.toList());
            restaurant.setOperatingModes(modes);
        }

        // 🏠 Address
        if (dto.getAddress() != null) {
            restaurant.setAddress(addressInfoMapper.toEntity(dto.getAddress()));
        }

        Restaurant saved = restaurantRepository.save(restaurant);
        return restaurantInfoMapper.toDto(saved);
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


        if (dto.getCategories() != null) {
            Set<RestaurantCategory> updatedCategories = dto.getCategories().stream()
                    .map(categoryId -> restaurantCategoryRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Category", id)))
                    .collect(Collectors.toSet());

            restaurant.setCategories(updatedCategories);
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
    public List<RestaurantForClientDto> getRestaurantsAvailableForClient() {
        Address address = clientService.getAddressByDefaultAddress();

        return restaurantRepository.getRestaurantsByCityAndArea(
                address.getArea()
        );
    }


//    @Override //For Client
//    public List<RestaurantForClientDto> getRestaurantsByCategoryId(Long categoryId) {
//        List<RestaurantForClientDto> allRestaurants = getAllRestaurantsForClient();
//
//        return allRestaurants.stream()
//                .filter(r -> r.getCategoryIds() != null && r.getCategoryIds().contains(categoryId))
//                .toList();
//    }
//
//    @Override //For Client
//    public List<RestaurantForClientDto> getAllRestaurantsSortedByDeliveryTime() {
//        List<RestaurantForClientDto> allRestaurants = getAllRestaurantsForClient();
//
//        allRestaurants.sort(Comparator.comparingInt(r -> {
//            try {
//                return Integer.parseInt(r.getDeliveryTime());
//            } catch (Exception e) {
//                return Integer.MAX_VALUE;
//            }
//        }));
//        // Добавить везде сортировку по рейтингу и рекомендации???
//        // Как сохранить точный порядок сортировки по рейтингу, рекомендации и время доставки???
//
//        return allRestaurants;
//    }

//    @Override
//    public Page<RestaurantForClientDto> searchRestaurants(
//            String query,
//            int page,
//            int size,
//            String sortBy,
//            String direction) {
//
//        Sort sort;
//        switch (sortBy.toLowerCase()) {
//            case "rating":
//                sort = Sort.by(Sort.Direction.fromString(direction), "rating");
//                break;
//            case "isrecommended":
//                sort = Sort.by(Sort.Direction.fromString(direction), "isRecommended");
//                break;
//            case "deliverytime":
//                sort = Sort.by(Sort.Direction.fromString(direction), "deliveryTime");
//                break;
//            default:
//                sort = Sort.by(Sort.Direction.fromString(direction), "id");
//        }
//
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        return restaurantRepository.searchClient(query, pageable)
//                .map(restaurantMapper::toRestaurantForClientDto);
//    }
}
