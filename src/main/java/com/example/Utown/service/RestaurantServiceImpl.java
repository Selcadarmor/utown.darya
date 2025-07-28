package com.example.Utown.service;

import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import com.example.Utown.dto.deliveryDTO.DeliveryInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeCreateDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeRestaurantProfileDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryInfoDto;
import com.example.Utown.dto.restaurantDTO.RestaurantCreateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;
import com.example.Utown.dto.restaurantDTO.RestaurantProfileDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateResponseDto;
import com.example.Utown.dto.restaurantDTO.RestaurantsCreateResponseDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.RestaurantCategoryInfoMapper;
import com.example.Utown.mapper.RestaurantInfoMapper;
import com.example.Utown.model.Address;
import com.example.Utown.model.Delivery;
import com.example.Utown.model.Dish;
import com.example.Utown.model.FileInfo;
import com.example.Utown.model.OperatingMode;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.RestaurantCategory;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.repository.DeliveryRepository;
import com.example.Utown.repository.DishRepository;
import com.example.Utown.repository.FileInfoRepository;
import com.example.Utown.repository.OperatingModeRepository;
import com.example.Utown.repository.RestaurantCategoryRepository;
import com.example.Utown.repository.RestaurantRepository;
import com.example.Utown.service.S3Service.FileInfoService;
import com.example.Utown.service.UserTypeService.ClientService;
import com.example.Utown.service.UserTypeService.RestaurantAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl  implements RestaurantService {

    private final ClientService clientService;
    private final RestaurantCategoryService restaurantCategoryService;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final RestaurantInfoMapper restaurantInfoMapper;
    private final RestaurantRepository restaurantRepository;
    private final OperatingModeService operatingModeService;
    private final OperatingModeRepository operatingModeRepository;
    private final RestaurantCategoryInfoMapper restaurantCategoryInfoMapper;
    private final RestaurantAdminService restaurantAdminService;
    private final AddressService addressService;
    private final DeliveryService deliveryService;
    private final DishRepository dishRepository;
    private final FileInfoService fileInfoService;
    private final FileInfoRepository fileInfoRepository;
    private final DeliveryRepository deliveryRepository;

    // ===== GET =====

    @Override
    public Page<RestaurantInfoDto> getAllRestaurants(String query, Boolean isActive,int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return restaurantRepository.findAllRestaurantsWithOrderCount(query, isActive,pageable);
    }

    @Override
    public RestaurantDetailsDto getRestaurantDetails(Long restaurantId) {
        Restaurant restaurantEntity = findRestaurantById(restaurantId);

        RestaurantDetailsDto restaurant = restaurantRepository.findRestaurantSummaryById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", restaurantId));

        Set<RestaurantCategoryInfoDto> categoryDtos = restaurantCategoryInfoMapper
                .toDtoSet(new HashSet<>(restaurantEntity.getCategories()));
        List<OperatingModeInfoDto> operatingModeDtos = operatingModeService.getOperatingModesByRestaurantId(restaurantId);
        List<DeliveryInfoDto> deliveryDtos = deliveryService.getDeliveriesByRestaurantId(restaurantId);

        restaurant.setCategories(categoryDtos);
        restaurant.setOperatingModes(operatingModeDtos);
        restaurant.setDeliveries(deliveryDtos);

        return restaurant;
    }

    @Override //For Client
    public Page<RestaurantForClientDto> getRecommendedRestaurantsForClient(Pageable pageable) {
        Client client = clientService.getCurrentClient();
        Address address = addressService.getAddressById(client.getDefaultAddress());

        return restaurantRepository.findRecommendedRestaurants(address.getState(), address.getCity(), address.getArea(), pageable);
    }

    @Override //For Client
    public Page<RestaurantForClientDto> getFastestDeliveryRestaurantsForClient(Pageable pageable) {
        Client client = clientService.getCurrentClient();
        Address address = addressService.getAddressById(client.getDefaultAddress());

        return restaurantRepository.findFastestDeliveryRestaurants(address.getState(), address.getCity(), address.getArea(), pageable);
    }

    @Override //For Client
    public Page<RestaurantForClientDto> getRestaurantsByCategory(Long categoryId, Pageable pageable) {
        Client client = clientService.getCurrentClient();
        Address address = addressService.getAddressById(client.getDefaultAddress());

        return restaurantRepository.findRestaurantsByCategory(address.getState(), address.getCity(), address.getArea(), categoryId, pageable);
    }

    @Override
    public Page<RestaurantForClientDto> searchRestaurants(
            String query,
            int page,
            int size,
            String sortBy,
            String direction) {

        Client client = clientService.getCurrentClient();
        Address address = addressService.getAddressById(client.getDefaultAddress());

        Sort sort = Sort.by(Sort.Direction.DESC, "isRecommended");

        if (sortBy != null) {
            switch (sortBy.toLowerCase()) {
                case "rating":
                    sort = Sort.by(Sort.Direction.fromString(direction), "rating");
                    break;
                case "deliverytime":
                    sort = Sort.by(Sort.Direction.fromString(direction), "deliveryTime");
                    break;
                case "isrecommended":
                    sort = Sort.by(Sort.Direction.fromString(direction), "isRecommended");
                    break;
            }
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        return restaurantRepository.searchClient(
                query,
                address.getState(),
                address.getCity(),
                address.getArea(),
                pageable
        );
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantProfileDto getRestaurantProfile(Long restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        List<OperatingModeRestaurantProfileDto> operatingModes = operatingModeRepository.findRawOperatingModesByRestaurantId(restaurantId);
        return RestaurantProfileDto.builder()
                .id(restaurant.getId())
                .title(restaurant.getTitle())
                .phone(restaurant.getPhone())
                .filePath(restaurant.getFileInfo() != null ? restaurant.getFileInfo().getPath() : null)
                .description(restaurant.getDescription())
                .deliveryTime(restaurant.getDeliveryTime())
                .totalRating(restaurant.getTotalRatings())
                .minOrderAmount(restaurant.getMinOrderAmount())
                .operatingModes(operatingModes)
                .build();
    }


    // ===== CREATE =====

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public RestaurantsCreateResponseDto createRestaurant(RestaurantCreateDto dto) {
        Restaurant restaurant = restaurantInfoMapper.toEntity(dto);

        if (dto.getFileId() != null) {
            Optional<FileInfo> fileOpt = fileInfoRepository.findById(dto.getFileId());
            fileOpt.ifPresent(restaurant::setFileInfo);
        }

        if (dto.getCategories() != null && !dto.getCategories().isEmpty()) {
            Set<RestaurantCategory> categories = restaurantCategoryService.resolveCategories(dto.getCategories());
            restaurant.setCategories(categories);
        }

        if (dto.getAddress() != null) {
            Address address = addressService.createAddress(dto.getAddress());
            restaurant.setAddress(address);
        }

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        RestaurantAdmin admin;
        if (dto.getRestaurantAdmin() != null) {
            admin = restaurantAdminService.createAdmin(dto.getRestaurantAdmin(), savedRestaurant);
            savedRestaurant.setRestaurantAdmin(admin);
        }

        restaurantRepository.save(savedRestaurant);

        if (dto.getOperatingModes() != null) {
            for (OperatingModeCreateDto modeDto : dto.getOperatingModes()) {
                modeDto.setRestaurantId(savedRestaurant.getId());
                operatingModeService.createOperatingMode(modeDto);
            }
        }
        if (dto.getDeliveries() != null) {
            for (DeliveryDto deliveryDto : dto.getDeliveries()) {
                deliveryDto.setRestaurantId(savedRestaurant.getId());
                deliveryService.createDelivery(deliveryDto);
            }
        }
        List<OperatingMode> operatingModes = operatingModeRepository.findByRestaurantId(savedRestaurant.getId());
        savedRestaurant.setOperatingModes(new ArrayList<>(operatingModes));

        List<Delivery> deliveries = deliveryRepository.findByRestaurantId(savedRestaurant.getId());
        savedRestaurant.setDeliveries(new ArrayList<>(deliveries));

        // Возвращаем DTO с уже подгруженными связями
        return restaurantInfoMapper.toCreateDto(savedRestaurant);
    }

    // ===== UPDATE =====

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public RestaurantUpdateResponseDto updateRestaurant(Long id, RestaurantUpdateDto dto) {
        Restaurant restaurant = findRestaurantById(id);

        restaurantInfoMapper.updateFromDto(dto, restaurant);

        if (dto.getFileId() != null) {
            fileInfoRepository.findById(dto.getFileId())
                    .ifPresent(restaurant::setFileInfo);
        } else {
            restaurant.setFileInfo(null); // очистить файл, если fileId == null
        }


        Address currentAddress = restaurant.getAddress();
        if (currentAddress == null) {
            Address newAddress = addressService.createAddress(dto.getAddress());
            restaurant.setAddress(newAddress);
        } else {
            if (currentAddress.getId() == null) {
                // Создаем новый адрес, потому что id нет
                Address newAddress = addressService.createAddress(dto.getAddress());
                restaurant.setAddress(newAddress);
            } else {
                addressService.updateAddress(currentAddress.getId(), dto.getAddress());
            }
        }


        if (dto.getOperatingModes() != null && !dto.getOperatingModes().isEmpty()) {
            operatingModeService.updateOperatingModes(dto.getOperatingModes());
        }

        if (dto.getDeliveries() != null && !dto.getDeliveries().isEmpty()) {
            deliveryService.updateDeliveriesByRestaurant(restaurant, dto.getDeliveries());
        }

        if (dto.getCategoryIds() != null) {
            if (dto.getCategoryIds().isEmpty()) {
                restaurant.getCategories().clear();
            } else {
                Set<RestaurantCategory> updatedCategories = dto.getCategoryIds().stream()
                        .map(categoryId -> restaurantCategoryRepository.findById(categoryId)
                                .orElseThrow(() -> new ResourceNotFoundException("RestaurantCategory", categoryId)))
                        .collect(Collectors.toSet());
                restaurant.setCategories(updatedCategories);
            }
        }

        Restaurant saved = restaurantRepository.save(restaurant);
        return restaurantInfoMapper.toUpdateDto(saved);
    }

    // ===== DELETE / DEACTIVATE =====

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void deactivateRestaurant(Long restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        restaurant.setIsActive(false);

        if (restaurant.getRestaurantAdmin() != null) {
            restaurant.getRestaurantAdmin().setActive(false);
        }

        if (restaurant.getDeliveries() != null) {
            restaurant.getDeliveries().forEach(delivery -> delivery.setIsActive(false));
        }

        List<Dish> dishes = dishRepository.findAllByRestaurantId(restaurantId);
        dishes.forEach(dish -> dish.setIsActive(false));
        dishRepository.saveAll(dishes);

        restaurantRepository.save(restaurant);
    }

    // ===== HELPERS =====

    public Restaurant findRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", restaurantId));
    }

}
