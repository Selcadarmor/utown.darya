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
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.exception.RestaurantNotFoundException;
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
import com.example.Utown.repository.DishRepository;
import com.example.Utown.repository.FileInfoRepository;
import com.example.Utown.repository.OperatingModeRepository;
import com.example.Utown.repository.RestaurantCategoryRepository;
import com.example.Utown.repository.RestaurantRepository;
import com.example.Utown.service.UserType.client.ClientService;
import com.example.Utown.service.UserType.client.RestaurantAdminService;
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
    private  final AddressService addressService;
    private  final DeliveryService deliveryService;
    private final DishRepository dishRepository;
    private final FileInfoService fileInfoService;
    private final FileInfoRepository fileInfoRepository;

    @Override//сделано
    public Page<RestaurantInfoDto> getAllRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return restaurantRepository.findAllRestaurantsWithOrderCount(pageable);
    }

    @Override
    public RestaurantDetailsDto getRestaurantDetails(Long restaurantId) {
        Restaurant restaurantEntity = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", restaurantId));

        RestaurantDetailsDto restaurant = restaurantRepository.findRestaurantSummaryById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", restaurantId));

        List<RestaurantCategoryInfoDto> categoryDtos = restaurantCategoryInfoMapper
                .toDtoList(new ArrayList<>(restaurantEntity.getCategories()));
        List<OperatingModeInfoDto> operatingModeDtos = operatingModeService.getOperatingModesByRestaurantId(restaurantId);
        List<DeliveryInfoDto> deliveryDtos = deliveryService.getDeliveriesByRestaurantId(restaurantId);
        restaurant.setCategories(categoryDtos);
        restaurant.setOperatingModes(operatingModeDtos);
        restaurant.setDeliveries(deliveryDtos);
        return restaurant;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public RestaurantDetailsDto createRestaurant(RestaurantCreateDto dto) {
        // 1. Преобразуем DTO в сущность ресторана (без админа)
        Restaurant restaurant = restaurantInfoMapper.toEntity(dto);

        // 2. Обработка файла
        FileInfo file = null;
        if (dto.getFileId() != null) {
            file = fileInfoRepository.findById(dto.getFileId()).orElse(null);
        }
        restaurant.setFileInfo(file);

        // 3. Обработка категорий
        List<RestaurantCategory> categories =
                restaurantCategoryService.findCategoriesByIds(dto.getCategories());
        restaurant.setCategories(new HashSet<>(categories));

        // 4. Обработка адреса
        if (dto.getAddress() != null) {
            Address address = addressService.createAddress(dto.getAddress());
            restaurant.setAddress(address);
        }

        // 5. Сохраняем ресторан без админа (теперь у ресторана будет id)
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        // 6. Создаём админа и связываем с рестораном
        RestaurantAdmin admin = restaurantAdminService.createAdmin(dto.getRestaurantAdmin(), savedRestaurant);

// 7. Связываем ресторан с админом
        savedRestaurant.setRestaurantAdmin(admin);

// 8. Сохраняем ресторан (если нужно обновить связь restaurant → admin)
        restaurantRepository.save(savedRestaurant);
        // 8. Обработка режимов работы
        if (dto.getOperatingModes() != null && !dto.getOperatingModes().isEmpty()) {
            for (OperatingModeCreateDto modeDto : dto.getOperatingModes()) {
                OperatingMode mode = new OperatingMode();
                mode.setStartTime(modeDto.getStartTime());
                mode.setEndTime(modeDto.getEndTime());
                mode.setDayOff(modeDto.isDayOff());
                mode.setDayOfWeek(modeDto.getDayOfWeek());

                // Устанавливаем связь с обеих сторон
                savedRestaurant.addOperatingMode(mode);
            }
        }

        // 9. Обработка доставок
        if (dto.getDeliveries() != null && !dto.getDeliveries().isEmpty()) {
            for (DeliveryDto deliveryDto : dto.getDeliveries()) {
                Delivery delivery = deliveryService.createDelivery(deliveryDto);
                savedRestaurant.addDelivery(delivery);
            }
        }

        // 10. Финальное сохранение ресторана со всеми связями
        savedRestaurant = restaurantRepository.save(savedRestaurant);

        // 11. Возврат DTO
        return restaurantInfoMapper.toDto(savedRestaurant);
    }




    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public RestaurantDetailsDto updateRestaurant(Long id, RestaurantUpdateDto dto) {
        Restaurant restaurant = findRestaurantById(id);

        restaurantInfoMapper.updateFromDto(dto, restaurant);

        if (dto.getFileInfoId() != null) {
            FileInfo fileInfo = fileInfoService.getFileInfoById(dto.getFileInfoId());
            restaurant.setFileInfo(fileInfo);
        }

        if (dto.getAddress() != null) {
            Address address = addressService.updateAddressByRestaurant(id, dto.getAddress());
        }

        if (dto.getOperatingModes() != null && !dto.getOperatingModes().isEmpty()) {
            operatingModeService.updateOperatingModes(restaurant, dto.getOperatingModes());
        }
        if (dto.getDeliveries() != null && !dto.getDeliveries().isEmpty()) {
            deliveryService.updateDeliveriesByRestaurant(restaurant, dto.getDeliveries());
        }

        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            List<RestaurantCategory> categories = new ArrayList<>();
            for (Long categoryId : dto.getCategoryIds()) {
                RestaurantCategory category = restaurantCategoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));
                categories.add(category);
            }
            restaurant.setCategories(new HashSet<>(categories));
        } else {
            restaurant.getCategories().clear();
        }

        Restaurant saved = restaurantRepository.save(restaurant);

        return restaurantInfoMapper.toDto(saved);
    }

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
        for (Dish dish : dishes) {
            dish.setIsActive(false);
        }
        dishRepository.saveAll(dishes);
        restaurantRepository.save(restaurant);
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

    @Override
    public Restaurant findRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
    }

}
