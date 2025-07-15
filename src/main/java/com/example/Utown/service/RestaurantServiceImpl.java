package com.example.Utown.service;

import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeCreateDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeUpdateDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
import com.example.Utown.dto.restaurantDTO.RestaurantCreateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.*;
import com.example.Utown.model.*;
import com.example.Utown.mapper.AddressInfoMapper;
import com.example.Utown.mapper.FileInfoMapper;
import com.example.Utown.mapper.RestaurantAdminInfoMapper;
import com.example.Utown.mapper.RestaurantInfoMapper;
import com.example.Utown.model.Delivery;
import com.example.Utown.model.OperatingMode;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.RestaurantCategory;
import com.example.Utown.model.Role;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.model.enumFiles.Roles;
import com.example.Utown.repository.*;
import com.example.Utown.service.UserType.client.ClientService;
import com.example.Utown.service.UserType.client.RestaurantAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl  implements RestaurantService {

    private final AddressInfoMapper addressInfoMapper;
    private final ClientService clientService;
    private final FileInfoMapper fileInfoMapper;
    private final OrderRepository orderRepository;
    private final RestaurantCategoryService restaurantCategoryService;
    private final RestaurantAdminInfoMapper restaurantAdminInfoMapper;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final RestaurantInfoMapper restaurantInfoMapper;
    private final RestaurantRepository restaurantRepository;
    private final OperatingModeService operatingModeService;
    private final RestaurantCategoryInfoMapper restaurantCategoryInfoMapper;
    private final OperatingModeInfoMapper operatingModeInfoMapper;
    private final DeliveryMapper deliveryMapper;
    private final RestaurantAdminService restaurantAdminService;
    private  final AddressService addressService;
    private  final OperatingModeRepository operatingModeRepository;
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

      //  if (dto.getRestaurantAdmin() != null) {
           // restaurantAdminInfoMapper.updateFromDto(dto.getRestaurantAdmin(), restaurant.getRestaurantAdmin());
       // }

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

    private Restaurant findRestaurantByIdOrThrow(Long restaurantId) { //  для переиспользования
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", restaurantId));
    }

    @Override //For Client
    public List<RestaurantForClientDto> getRestaurantsAvailableForClient() {
        Address address = clientService.getAddressByDefaultAddress();

        return restaurantRepository.getRestaurantsByCityAndArea(
                address.getArea()
        );
    }
}
