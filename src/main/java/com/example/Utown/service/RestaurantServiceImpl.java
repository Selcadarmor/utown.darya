package com.example.Utown.service;

import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeCreateDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeRestaurantProfileDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeUpdateDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import com.example.Utown.dto.restaurantDTO.RestaurantCreateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;
import com.example.Utown.dto.restaurantDTO.RestaurantProfileDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.exception.RestaurantNotFoundException;
import com.example.Utown.mapper.AddressInfoMapper;
import com.example.Utown.mapper.DeliveryMapper;
import com.example.Utown.mapper.FileInfoMapper;
import com.example.Utown.mapper.OperatingModeInfoMapper;
import com.example.Utown.mapper.RestaurantAdminInfoMapper;
import com.example.Utown.mapper.RestaurantCategoryInfoMapper;
import com.example.Utown.mapper.RestaurantInfoMapper;
import com.example.Utown.model.Address;
import com.example.Utown.model.Delivery;
import com.example.Utown.model.Dish;
import com.example.Utown.model.OperatingMode;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.RestaurantCategory;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.repository.AddressRepository;
import com.example.Utown.repository.DishRepository;
import com.example.Utown.repository.OperatingModeRepository;
import com.example.Utown.repository.OrderRepository;
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
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl  implements RestaurantService {

    private final AddressInfoMapper addressInfoMapper;
    private final AddressRepository addressRepository;
    private final ClientService clientService;
    private final FileInfoMapper fileInfoMapper;
    private final OrderRepository orderRepository;
    private final RestaurantCategoryService restaurantCategoryService;
    private final RestaurantAdminInfoMapper restaurantAdminInfoMapper;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final RestaurantInfoMapper restaurantInfoMapper;
    private final RestaurantRepository restaurantRepository;
    private final OperatingModeService operatingModeService;
    private final OperatingModeRepository operatingModeRepository;
    private final RestaurantCategoryInfoMapper restaurantCategoryInfoMapper;
    private final OperatingModeInfoMapper operatingModeInfoMapper;
    private final DeliveryMapper deliveryMapper;
    private final RestaurantAdminService restaurantAdminService;
    private  final AddressService addressService;
    private  final DeliveryService deliveryService;
    private final DishRepository dishRepository;


    @Override//сделано
    public Page<RestaurantInfoDto> getAllRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return restaurantRepository.findAllRestaurantsWithOrderCount(pageable);
    }


    @Override
    public RestaurantDetailsDto getRestaurantDetails(Long restaurantId) {
        Restaurant restaurant = findRestaurantByIdOrThrow(restaurantId);

        Long orderCount = orderRepository.countByRestaurantId(restaurantId);

        List<RestaurantCategoryDto> categoryDtos =
                restaurantCategoryInfoMapper.toDtoList(new ArrayList<>(restaurant.getCategories()));
        List<OperatingModeInfoDto> operatingModeDtos = operatingModeInfoMapper.toDtoList(restaurant.getOperatingModes());
        List<DeliveryDto> deliveryDtos = deliveryMapper.toDtoList(restaurant.getDeliveries());
        return new RestaurantDetailsDto(
                restaurant.getId(),
                restaurant.getTitle(),
                restaurant.getDescription(),
                restaurant.getPhone(),
                restaurant.getMinOrderAmount(),
                orderCount,
                restaurant.getFileInfo() != null ? restaurant.getFileInfo().getId() : null,
                restaurantCategoryInfoMapper.toDtoList(new ArrayList<>(restaurant.getCategories())),
                operatingModeInfoMapper.toDtoList(restaurant.getOperatingModes()),
                restaurant.getRestaurantAdmin() != null ? restaurantAdminInfoMapper.toDtos(restaurant.getRestaurantAdmin()) : null,
                deliveryMapper.toDtoList(restaurant.getDeliveries())
        );
    }


    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public RestaurantDetailsDto createRestaurant(RestaurantCreateDto dto) {
        Restaurant restaurant = restaurantInfoMapper.toEntity(dto);

        // 👤 Admin create
        RestaurantAdmin admin = restaurantAdminService.createAdmin(dto.getRestaurantAdmin());
        admin.setRestaurant(restaurant);
        restaurant.setRestaurantAdmin(admin);

        // 📁 File
        Optional.ofNullable(dto.getFileInfo())// проверяем есть в ли в дто файл если есть мапим и присваиваем ресторану
                .map(fileInfoMapper::toDetailsEntity).ifPresent(restaurant::setFileInfo);


        // 🏷 Categories
        if (dto.getCategories() != null && !dto.getCategories().isEmpty()) {
            List<RestaurantCategory> createdCategories =
                    restaurantCategoryService.createRestaurantCategories(dto.getCategories());

            restaurant.setCategories(new HashSet<>(createdCategories));
        }
        // 🏠 Address
        if (dto.getAddress() != null) {
            Address address = addressService.createAddress(dto.getAddress());
            restaurant.setAddress(address);
        }

        if (dto.getOperatingModes() != null && !dto.getOperatingModes().isEmpty()) {
            List<OperatingMode> modes = new ArrayList<>();
            for (OperatingModeCreateDto modeDto : dto.getOperatingModes()) {
                OperatingMode mode = new OperatingMode();
                mode.setRestaurant(restaurant);
                mode.setStart(modeDto.getStart());
                mode.setEnd(modeDto.getEnd());
                mode.setDayOff(modeDto.isDayOff());
                mode.setDayOfWeek(modeDto.getDayOfWeek());
                modes.add(mode);
            }

            List<OperatingMode> savedModes = operatingModeRepository.saveAll(modes);
            restaurant.setOperatingModes(savedModes);
        }
        if (dto.getDeliveries() != null && !dto.getDeliveries().isEmpty()) {
            List<Delivery> deliveries = new ArrayList<>();
            for (DeliveryDto deliveryDto : dto.getDeliveries()) {
                Delivery delivery = deliveryService.createDelivery(deliveryDto);
                delivery.setRestaurant(restaurant);
                deliveries.add(delivery);
            }
            restaurant.setDeliveries(new ArrayList<>(deliveries));
        }
        Restaurant saved = restaurantRepository.save(restaurant);
        return restaurantInfoMapper.toDto(saved);
    }


    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public RestaurantDetailsDto updateRestaurant(Long id, RestaurantUpdateDto dto) {
        Restaurant restaurant = findRestaurantByIdOrThrow(id);

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
    //обнавляю данные режима работы
        if (!CollectionUtils.isEmpty(dto.getOperatingModes())) {
            for (OperatingModeUpdateDto omDto : dto.getOperatingModes()) {
                operatingModeService.update(omDto.getId(), omDto);
            }
        }

        if (!CollectionUtils.isEmpty(dto.getCategories())) {
            Set<RestaurantCategory> categories = dto.getCategories().stream()
                    .map(catDto -> restaurantCategoryRepository.findById(catDto.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("RestaurantCategory", catDto.getId())))
                    .collect(Collectors.toSet());
            restaurant.setCategories(categories);
        }

        Restaurant saved = restaurantRepository.save(restaurant);

        return restaurantInfoMapper.toDto(saved);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void deactivateRestaurant(Long restaurantId) {
        Restaurant restaurant = findRestaurantByIdOrThrow(restaurantId);
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
    private Restaurant findRestaurantByIdOrThrow(Long restaurantId) { //  для переиспользования
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", restaurantId));
    }

    @Override //For Client
    public Page<RestaurantForClientDto> getRecommendedRestaurantsForClient(Pageable pageable) {
        Client client = clientService.getCurrentClient();
        Address address = addressRepository.findById(client.getDefaultAddress())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        return restaurantRepository.findRecommendedRestaurants(address.getCity(), address.getArea(), pageable);
    }

    @Override //For Client
    public Page<RestaurantForClientDto> getFastestDeliveryRestaurantsForClient(Pageable pageable) {
        Client client = clientService.getCurrentClient();
        Address address = addressRepository.findById(client.getDefaultAddress())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        return restaurantRepository.findFastestDeliveryRestaurants(address.getCity(), address.getArea(), pageable);
    }

    @Override //For Client
    public Page<RestaurantForClientDto> getRestaurantsByCategory(Long categoryId, Pageable pageable) {
        Client client = clientService.getCurrentClient();
        Address address = addressRepository.findById(client.getDefaultAddress())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        return restaurantRepository.findRestaurantsByCategory(address.getCity(), address.getArea(), categoryId, pageable);
    }

    @Override
    public Page<RestaurantForClientDto> searchRestaurants(
            String query,
            int page,
            int size,
            String sortBy,
            String direction) {

        Client client = clientService.getCurrentClient();
        Address address = addressRepository.findById(client.getDefaultAddress())
                .orElseThrow(() -> new RuntimeException("Address not found"));

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
                address.getCity(),
                address.getArea(),
                pageable
        );
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantProfileDto getRestaurantProfile(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
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



}
