package com.example.Utown.service;

import com.example.Utown.dto.operatingModeDTO.OperatingModeCreateDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeRestaurantProfileDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeUpdateDto;
import com.example.Utown.dto.restaurantDTO.*;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.exception.RestaurantNotFoundException;
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
import com.example.Utown.model.UserType.Client;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.model.enumFiles.Roles;
import com.example.Utown.repository.*;
import com.example.Utown.service.UserType.client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
    private final PasswordEncoder passwordEncoder;
    private final RestaurantAdminInfoMapper restaurantAdminInfoMapper;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final RestaurantInfoMapper restaurantInfoMapper;
    private final RestaurantRepository restaurantRepository;
    private final RoleRepository roleRepository;
    private final OperatingModeService operatingModeService;
    private final OperatingModeRepository operatingModeRepository;


    @Override//сделано
    public Page<RestaurantInfoDto> getAllRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return restaurantRepository.findAllRestaurantsWithOrderCount(pageable);
    }


    @Override
    public RestaurantDetailsDto getRestaurantById(Long id) { // сделала
        Restaurant restaurant = findRestaurantByIdOrThrow(id); //вытаскиваем ресторан из базы и проверяем на ошибки
        Long orderCounts = orderRepository.countByRestaurantId(id); //подсчитываем количество заказов

        List<Long> categoryIds = restaurant.getCategories().stream()
                .map(RestaurantCategory::getId)
                .toList();//по айди подтягиваем  категорию так как в репозитории нельзя из списка вытаскивать
        List<Long> operatingModeIds = restaurant.getOperatingModes().stream()
                .map(OperatingMode::getId)
                .toList(); // то же из списка по айли вытаскиваем operatingMode
        List<Long> deliveryIds = restaurant.getDeliveries().stream()
                .map(Delivery::getId)
                .toList(); //  тут область доставки
        return new RestaurantDetailsDto(
                restaurant.getId(),
                restaurant.getTitle(),
                restaurant.getDescription(),
                restaurant.getPhone(),
                restaurant.getMinOrderAmount(),
                restaurant.getFileInfo() != null ? restaurant.getFileInfo().getId() : null,
                orderCounts,
                categoryIds,
                operatingModeIds,
                restaurant.getRestaurantAdmin() != null ? restaurant.getRestaurantAdmin().getId() : null,
                deliveryIds
        );// собираем в дто
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestaurantDetailsDto createRestaurant(RestaurantCreateDto dto) {
        Restaurant restaurant = restaurantInfoMapper.toEntity(dto);

        // 👤 Admin create
        RestaurantAdmin admin = restaurantAdminInfoMapper.toEntity(dto.getRestaurantAdmin());
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        Role role = roleRepository.findByName(Roles.ROLE_RESTAURANT_ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "ROLE_RESTAURANT_ADMIN"));

        admin.setRoles(Set.of(role));
        admin.setRestaurant(restaurant);
        restaurant.setRestaurantAdmin(admin);

        // 📁 File
        Optional.ofNullable(dto.getFileInfo())// проверяем есть в ли в дто файл если есть мапим и присваиваем ресторану
                .map(fileInfoMapper::toDetailsEntity).ifPresent(restaurant::setFileInfo);


        // 🏷 Categories
        if (!CollectionUtils.isEmpty(dto.getCategories())) { // проверяем что список не нал и не пуст и для каждого элемента вытаскиваем категорию которая соответствует ей и собираем в сет
            Set<RestaurantCategory> categories = dto.getCategories().stream()
                    .map(id -> restaurantCategoryRepository.findById(id.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("RestaurantCategory", id)))
                    .collect(Collectors.toSet());
            restaurant.setCategories(categories);
        }

        // 🏠 Address
        if (dto.getAddress() != null) {
            restaurant.setAddress(addressInfoMapper.toEntity(dto.getAddress()));
        }
        Restaurant saved = restaurantRepository.save(restaurant); // сначала сохраняем ресторан что б получить айди и потом по ади ставим ему режим
        List<Long> createdModeIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dto.getOperatingModes())) {
            for (OperatingModeCreateDto createDTO : dto.getOperatingModes()) {
                createDTO.setRestaurantId(saved.getId());
                OperatingModeInfoDto created = operatingModeService.create(createDTO);
                createdModeIds.add(created.getId());
            }
        }

        RestaurantDetailsDto resultDto = restaurantInfoMapper.toDto(saved);
        resultDto.setOperatingModeIds(createdModeIds);
        return resultDto;

    }

    @Transactional(rollbackFor = Exception.class)
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = findRestaurantByIdOrThrow(id);
        restaurantRepository.delete(restaurant);
    }

    private Restaurant findRestaurantByIdOrThrow(Long restaurantId) { //  для переиспользования
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", restaurantId));
    }

    @Override //For Client
    public Page<RestaurantForClientDto> getRecommendedRestaurantsForClient(Pageable pageable) {
        Client client = clientService.getCurrentClient();
        Address address = addressRepository.findById(client.getDefaultAddress())
                .orElseThrow(() -> new RuntimeException("Адрес клиента не найден"));

        return restaurantRepository.findRecommendedRestaurants(address.getCity(), address.getArea(), pageable);
    }

    @Override //For Client
    public Page<RestaurantForClientDto> getFastestDeliveryRestaurantsForClient(Pageable pageable) {
        Client client = clientService.getCurrentClient();
        Address address = addressRepository.findById(client.getDefaultAddress())
                .orElseThrow(() -> new RuntimeException("Адрес клиента не найден"));

        return restaurantRepository.findFastestDeliveryRestaurants(address.getCity(), address.getArea(), pageable);
    }

    @Override //For Client
    public Page<RestaurantForClientDto> getRestaurantsByCategory(Long categoryId, Pageable pageable) {
        Client client = clientService.getCurrentClient();
        Address address = addressRepository.findById(client.getDefaultAddress())
                .orElseThrow(() -> new RuntimeException("Адрес клиента не найден"));

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
                .orElseThrow(() -> new RuntimeException("Адрес клиента не найден"));

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
