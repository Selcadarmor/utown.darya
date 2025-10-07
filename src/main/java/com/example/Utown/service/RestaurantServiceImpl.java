package com.example.Utown.service;

import com.example.Utown.config.S3.AwsProperties;
import com.example.Utown.dto.LocationRequestDto;
import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.addressDTO.AddressResponseDto;
import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import com.example.Utown.dto.deliveryDTO.DeliveryInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeCreateDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeRestaurantProfileDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import com.example.Utown.dto.restaurantDTO.RestaurantCreateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;
import com.example.Utown.dto.restaurantDTO.RestaurantProfileDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateResponseDto;
import com.example.Utown.dto.restaurantDTO.RestaurantsCreateResponseDto;
import com.example.Utown.exception.InvalidArgumentException;
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
import com.example.Utown.model.enumFiles.RestaurantStatus;
import com.example.Utown.repository.DeliveryRepository;
import com.example.Utown.repository.DishRepository;
import com.example.Utown.repository.FileInfoRepository;
import com.example.Utown.repository.OperatingModeRepository;
import com.example.Utown.repository.RatingRepository;
import com.example.Utown.repository.RestaurantCategoryRepository;
import com.example.Utown.repository.RestaurantRepository;
import com.example.Utown.service.KafkaService.LocationKafkaService;
import com.example.Utown.service.S3Service.FileInfoService;
import com.example.Utown.service.UserTypeService.ClientService;
import com.example.Utown.service.UserTypeService.RestaurantAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantServiceImpl  implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AddressService addressService;
    private final AwsProperties awsProperties;
    private final ClientService clientService;
    private final DeliveryService deliveryService;
    private final DeliveryRepository deliveryRepository;
    private final DishRepository dishRepository;
    private final FileInfoRepository fileInfoRepository;
    private final FileInfoService fileInfoService;
    private final OperatingModeService operatingModeService;
    private final OperatingModeRepository operatingModeRepository;
    private final RatingRepository ratingRepository;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final RestaurantInfoMapper restaurantInfoMapper;
    private final RestaurantCategoryInfoMapper restaurantCategoryInfoMapper;
    private final RestaurantAdminService restaurantAdminService;
    private final LocationKafkaService locationKafkaService;

    // ===== GET =====

    @Override
    public Restaurant findRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> {
                    log.error("Restaurant not found by id: {}", restaurantId);
                    return new ResourceNotFoundException("Restaurant", restaurantId);
                });
    }

    @Override
    public Page<RestaurantInfoDto> getAllRestaurants(String query, Boolean isActive,int page, int size) {
        log.info("getAllRestaurants query: {}, isActive: {}, page: {}, size: {}", query, isActive, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return restaurantRepository.findAllRestaurantsWithOrderCount(query, isActive,pageable);
    }

    @Override
    public RestaurantDetailsDto getRestaurantDetails(Long restaurantId) {
        log.info("getRestaurantDetails restaurantId: {}", restaurantId);
        Restaurant restaurantEntity = findRestaurantById(restaurantId);

        RestaurantDetailsDto restaurant = restaurantRepository.findRestaurantSummaryById(restaurantId)
                .orElseThrow(() -> {
                    log.error("Restaurant not found by id: {}", restaurantId);
                    return new ResourceNotFoundException("Restaurant", restaurantId);
                });

        if (restaurant.getPath() != null && !restaurant.getPath().isEmpty()) {
            log.info("getRestaurantDetails restaurant.getPath(): {}", restaurant.getPath());
            String url = awsProperties.getPublicBaseUrl() + "/" + restaurant.getPath();
            restaurant.setFileUrl(url);
            log.info("getRestaurantDetails restaurant.getFileUrl(): {}", restaurant.getFileUrl());
        }

        Set<RestaurantCategoryDto> categoryDtos = restaurantCategoryInfoMapper
                .toDtoSet(new HashSet<>(restaurantEntity.getCategories()));
        List<OperatingModeInfoDto> operatingModeDtos = operatingModeService.getOperatingModesByRestaurantId(restaurantId);
        List<DeliveryInfoDto> deliveryDtos = deliveryService.getDeliveriesByRestaurantId(restaurantId);

        restaurant.setCategories(categoryDtos);
        restaurant.setOperatingModes(operatingModeDtos);
        restaurant.setDeliveries(deliveryDtos);
        log.info("getRestaurantDetails restaurant: {}", restaurant);

        return restaurant;
    }

    @Override //For Client
    public Page<RestaurantForClientDto> getRecommendedRestaurantsForClient(Pageable pageable) {
        log.info("getRecommendedRestaurantsForClient pageable: {}", pageable);
        Client client = clientService.getCurrentClient();
        Address address = addressService.getAddressById(client.getDefaultAddress());
        log.info("getRecommendedRestaurantsForClient address: {}", address);
        return restaurantRepository.findRecommendedRestaurants(address.getState(), address.getCity(), address.getArea(), pageable);
    }

    @Override //For Client
    public Page<RestaurantForClientDto> getFastestDeliveryRestaurantsForClient(Pageable pageable) {
        log.info("getFastestDeliveryRestaurantsForClient pageable: {}", pageable);
        Client client = clientService.getCurrentClient();
        Address address = addressService.getAddressById(client.getDefaultAddress());
        log.info("getFastestDeliveryRestaurantsForClient address: {}", address);

        return restaurantRepository.findFastestDeliveryRestaurants(address.getState(), address.getCity(), address.getArea(), pageable);
    }

    @Override //For Client
    public Page<RestaurantForClientDto> getRestaurantsByCategory(Long categoryId, Pageable pageable) {
        log.info("getRestaurantsByCategory categoryId: {}, pageable: {}", categoryId, pageable);
        Client client = clientService.getCurrentClient();
        Address address = addressService.getAddressById(client.getDefaultAddress());
        log.info("getRestaurantsByCategory address: {}", address);

        return restaurantRepository.findRestaurantsByCategory(address.getState(), address.getCity(), address.getArea(), categoryId, pageable);
    }

    @Override
    public Page<RestaurantForClientDto> searchRestaurants(
            String query,
            int page,
            int size,
            String sortBy,
            String direction) {
        log.info("searchRestaurants query: {}, page: {}, size: {}, sortBy: {}, direction: {}", query, page, size, sortBy, direction);

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
        log.info("searchRestaurants pageable: {}", pageable);

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
        log.info("getRestaurantProfile restaurantId: {}", restaurantId);
        Restaurant restaurant = findRestaurantById(restaurantId);
        List<OperatingModeRestaurantProfileDto> operatingModes = operatingModeRepository.findRawOperatingModesByRestaurantId(restaurantId);
        log.info("getRestaurantProfile operatingModes: {}", operatingModes);
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
        log.info("createRestaurant dto: {}", dto);

        Restaurant restaurant = restaurantInfoMapper.toEntity(dto);
        restaurant.setIsActive(true);

        // Файл
        if (dto.getFileId() != null) {
            fileInfoRepository.findById(dto.getFileId()).ifPresent(restaurant::setFileInfo);
        }

        // Категории
        restaurant.setCategories(resolveCategoriesByIds(dto.getCategoryIds()));

        // Асинхронный адрес
        if (dto.getAddress() != null) {
            Address tempAddress = addressService.createAddress(dto.getAddress());
            restaurant.setAddress(tempAddress);
            log.info("Created temporary address and sent to Kafka: {}", tempAddress);
        }

        // Сохраняем ресторан без ожидания ответа Kakao
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        log.info("Saved restaurant: {}", savedRestaurant);

        // Создаём админа ресторана
        if (dto.getRestaurantAdmin() == null) {
            throw new InvalidArgumentException("restaurantAdmin", "Restaurant admin data must be provided");
        }
        RestaurantAdmin admin = restaurantAdminService.createAdmin(dto.getRestaurantAdmin(), savedRestaurant);
        savedRestaurant.setRestaurantAdmin(admin);
        restaurantRepository.save(savedRestaurant);

        // Операционные режимы
        if (dto.getOperatingModes() != null) {
            for (OperatingModeCreateDto modeDto : dto.getOperatingModes()) {
                modeDto.setRestaurantId(savedRestaurant.getId());
                operatingModeService.createOperatingMode(modeDto);
            }
        }

        // Доставки
        if (dto.getDeliveries() != null) {
            for (DeliveryDto deliveryDto : dto.getDeliveries()) {
                deliveryDto.setRestaurantId(savedRestaurant.getId());
                deliveryService.createDelivery(deliveryDto);
            }
        }

        // Подгружаем связи для ответа
        savedRestaurant.setOperatingModes(new ArrayList<>(operatingModeRepository.findByRestaurantId(savedRestaurant.getId())));
        savedRestaurant.setDeliveries(new ArrayList<>(deliveryRepository.findByRestaurantId(savedRestaurant.getId())));

        // Отправка запроса в сервис для отслеживания (Kafka)
        LocationRequestDto requestDto = new LocationRequestDto();
        requestDto.setEntityId(savedRestaurant.getId());
        requestDto.setEntityType("RESTAURANT");
        if (savedRestaurant.getAddress() != null) {
            requestDto.setAddress(savedRestaurant.getAddress().getFullAddress() != null
                    ? savedRestaurant.getAddress().getFullAddress() : "");
        }
        locationKafkaService.send(requestDto);

        return restaurantInfoMapper.toCreateDto(savedRestaurant);
    }
    // ===== UPDATE =====

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public RestaurantUpdateResponseDto updateRestaurant(Long id, RestaurantUpdateDto dto) {
        log.info("Updating restaurant with id: {}", id);

        Restaurant restaurant = findRestaurantById(id);
        log.debug("Found restaurant: {}", restaurant.getTitle());


        restaurantInfoMapper.updateFromDto(dto, restaurant);
        log.debug("Restaurant basic info updated from DTO");

        if (dto.getFileId() != null) {
            FileInfo file = fileInfoService.getFileInfoById(dto.getFileId());
            restaurant.setFileInfo(file);
            log.debug("Updated file info with id: {}", dto.getFileId());
        }

        AddressDto addressDto = dto.getAddress();
        if (addressDto != null) {
            Address currentAddress = restaurant.getAddress();
            if (currentAddress == null || currentAddress.getId() == null) {
                // Создаем новый адрес и отправляем в Kakao
                Address newAddress = addressService.createAddress(addressDto);
                restaurant.setAddress(newAddress);
                log.debug("Created and set new address: {}", newAddress);
            } else {
                // Обновляем существующий адрес и отправляем изменения в Kakao
                Address updatedAddress = addressService.updateAddress(currentAddress.getId(), addressDto);
                restaurant.setAddress(updatedAddress);
                log.debug("Updated existing address and sent update to Kakao: {}", updatedAddress.getId());
            }
        } else {
            log.debug("No address data provided in DTO — skipping address update.");
        }


        if (dto.getOperatingModes() != null && !dto.getOperatingModes().isEmpty()) {
            operatingModeService.updateOperatingModes(dto.getOperatingModes());
            log.debug("Updated operating modes");
        }

        if (dto.getDeliveries() != null && !dto.getDeliveries().isEmpty()) {
            deliveryService.updateDeliveriesByRestaurant(restaurant, dto.getDeliveries());
            log.debug("Updated deliveries");
        }

        if (dto.getCategoryIds() != null) {
            if (dto.getCategoryIds().isEmpty()) {
                restaurant.getCategories().clear();
                log.debug("Cleared all restaurant categories");
            } else {
                Set<RestaurantCategory> categories = resolveCategoriesByIds(dto.getCategoryIds());
                restaurant.setCategories(categories);
                log.debug("Updated restaurant categories: {}", dto.getCategoryIds());
            }
        }

        Restaurant saved = restaurantRepository.save(restaurant);
        log.info("Restaurant with id {} successfully updated", saved.getId());

        return restaurantInfoMapper.toUpdateDto(saved);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public RestaurantUpdateResponseDto updateRestaurantByAdmin(RestaurantUpdateDto dto) {
        RestaurantAdmin currentAdmin = restaurantAdminService.getCurrentAdmin();
        Long restaurantId = currentAdmin.getRestaurant().getId();
        log.info("Updating restaurant with id: {}", restaurantId);

        return updateRestaurant(restaurantId, dto);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateStatusForCurrentAdminRestaurant(RestaurantStatus newStatus) {
        RestaurantAdmin currentAdmin = restaurantAdminService.getCurrentAdmin();
        Restaurant restaurant = findRestaurantById(currentAdmin.getRestaurant().getId());
        log.info("Updating restaurant status for current admin restaurant: {}", restaurant.getTitle());

        validationStatusChange(restaurant.getStatus(), newStatus);
        log.info("Validation status change for current admin restaurant: {}", restaurant.getStatus());

        restaurant.setStatus(newStatus);
        log.info("Updated restaurant status for current admin restaurant: {}", restaurant.getStatus());
        restaurantRepository.save(restaurant);
    }

    private void validationStatusChange(RestaurantStatus currentStatus, RestaurantStatus newStatus) {

        if (currentStatus == null) {
            log.info("Current status is null, allowing change to {}", newStatus);
            // Например, считаем, что если статус отсутствует — смена возможна на любой статус
            return;
        }

        if (currentStatus == newStatus) {
            log.warn("Attempted to change restaurant status to the same value: {}", newStatus);
            throw new InvalidArgumentException("Restaurant", newStatus.toString());
        }

        switch (currentStatus) {
            case NOT_ACTIVE:
                if (newStatus != RestaurantStatus.CLOSE) {
                    log.warn("Invalid status change from NOT_ACTIVE to {}", newStatus);
                    throw new InvalidArgumentException("Restaurant", newStatus.toString());
                }
                break;

            case CLOSE:
                if (newStatus != RestaurantStatus.OPEN && newStatus != RestaurantStatus.NOT_ACTIVE) {
                    log.warn("Invalid status change from CLOSE to {}", newStatus);
                    throw new InvalidArgumentException("Restaurant", newStatus.toString());
                }
                break;
            case OPEN:
                if (newStatus != RestaurantStatus.CLOSE) {
                    log.warn("Invalid status change from OPEN to {}", newStatus);
                    throw new InvalidArgumentException("Restaurant", newStatus.toString());
                }
                break;
            default:
                log.error("Unknown current status: {}", currentStatus);
                throw new InvalidArgumentException("Restaurant", currentStatus.toString());
        }
        log.info("Status change from {} to {} is valid", currentStatus, newStatus);
    }

    @Override
    @Transactional
    public void updateRestaurantRating(Restaurant restaurant) {
        Long restaurantId = restaurant.getId();
        Integer count = ratingRepository.countByRestaurantId(restaurantId);
        BigDecimal avg = ratingRepository.averageGradeByRestaurantId(restaurantId);

        restaurant.setTotalRatings(count);
        restaurant.setRating(avg != null ? avg : BigDecimal.ZERO);
        restaurantRepository.save(restaurant);
    }

    // ===== DELETE / DEACTIVATE =====

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void deactivateRestaurant(Long restaurantId) {
        log.info("deactivateRestaurant restaurantId: {}", restaurantId);
        Restaurant restaurant = findRestaurantById(restaurantId);
        restaurant.setIsActive(false);

        log.debug("Restaurant with id {} marked as inactive", restaurantId);

        if (restaurant.getRestaurantAdmin() != null) {
            restaurant.getRestaurantAdmin().setIsActive(false);
            log.debug("RestaurantAdmin with id {} marked as inactive", restaurant.getRestaurantAdmin().getId());
        } else {
            log.warn("RestaurantAdmin is null for restaurant with id {}", restaurantId);
        }

        if (restaurant.getDeliveries() != null) {
            restaurant.getDeliveries().forEach(delivery -> delivery.setIsDeleted(true));
            log.debug("All deliveries for restaurant id {} marked as deleted", restaurantId);
        } else {
            log.debug("Restaurant with id {} has no deliveries to delete", restaurantId);

        }

        List<Dish> dishes = dishRepository.findAllByRestaurantId(restaurantId);
        dishes.forEach(dish -> dish.setIsActive(false));
        dishRepository.saveAll(dishes);
        log.debug("All dishes for restaurant id {} marked as inactive", restaurantId);

        restaurantRepository.save(restaurant);
        log.info("Restaurant with id {} successfully deactivated", restaurantId);
    }

    // ===== HELPERS =====



    private Set<RestaurantCategory> resolveCategoriesByIds(Set<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            log.info("resolveCategoriesByIds categoryIds is null or empty");
            return Collections.emptySet();
        }

        Set<RestaurantCategory> categories = new HashSet<>();

        for (Long id : categoryIds) {
            RestaurantCategory category = restaurantCategoryRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("RestaurantCategory not found by id: {}", id);
                        return new ResourceNotFoundException("RestaurantCategory", id);
                    });
            categories.add(category);
            log.info("resolveCategoriesByIds category: {}", category);
        }

        return categories;
    }

}
