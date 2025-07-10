package com.example.Utown.service;

import com.example.Utown.dto.operatingModeDTO.OperatingModeCreateDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
import com.example.Utown.dto.restaurantDTO.RestaurantCreateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.AddressInfoMapper;
import com.example.Utown.mapper.FileInfoMapper;
import com.example.Utown.mapper.RestaurantAdminInfoMapper;
import com.example.Utown.mapper.RestaurantInfoMapper;
import com.example.Utown.mapper.RestaurantMapper;
import com.example.Utown.model.Delivery;
import com.example.Utown.model.OperatingMode;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.RestaurantCategory;
import com.example.Utown.model.Role;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.model.enumFiles.Roles;
import com.example.Utown.repository.OrderRepository;
import com.example.Utown.repository.RestaurantCategoryRepository;
import com.example.Utown.repository.RestaurantRepository;
import com.example.Utown.repository.RoleRepository;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
    private final FileInfoMapper fileInfoMapper;
    private final OrderRepository orderRepository;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final OperatingModeService operatingModeService;


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
                .toList(); // то же из списка по айди вытаскиваем operatingMode
        List<Long>  deliveryIds = restaurant.getDeliveries().stream()
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

    @Override //For Client
    public List<RestaurantForClientDto> getAllRestaurantsForClient() {
        List<Restaurant> restaurants = restaurantRepository.getAllActiveRestaurantsForClient();
        return restaurants.stream()
                .map(restaurantMapper::toRestaurantForClientDto)
                .collect(Collectors.toList());
        // добавить сортировку по рейтингу???
        // добавить сортировку по рекомендации???
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
        // Добавить везде сортировку по рейтингу и рекомендации???
        // Как сохранить точный порядок сортировки по рейтингу, рекомендации и время доставки???

        return allRestaurants;
    }

    @Override
    public Page<RestaurantForClientDto> searchRestaurants(
            String query,
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort;
        switch (sortBy.toLowerCase()) {
            case "rating":
                sort = Sort.by(Sort.Direction.fromString(direction), "rating");
                break;
            case "isrecommended":
                sort = Sort.by(Sort.Direction.fromString(direction), "isRecommended");
                break;
            case "deliverytime":
                sort = Sort.by(Sort.Direction.fromString(direction), "deliveryTime");
                break;
            default:
                sort = Sort.by(Sort.Direction.fromString(direction), "id");
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        return restaurantRepository.searchClient(query, pageable)
                .map(restaurantMapper::toRestaurantForClientDto);
    }

    private Restaurant findRestaurantByIdOrThrow(Long restaurantId) { //  для переиспользования
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", restaurantId));
    }
}
