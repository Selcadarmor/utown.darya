package com.example.Utown.service;

import com.example.Utown.config.S3.AwsProperties;
import com.example.Utown.dto.dishDTO.DishCreateDto;
import com.example.Utown.dto.dishDTO.DishDeletedMenuDto;
import com.example.Utown.dto.dishDTO.DishDetailsDto;
import com.example.Utown.dto.dishDTO.DishInfoDto;
import com.example.Utown.dto.dishDTO.DishMenuDto;
import com.example.Utown.dto.dishDTO.DishSearchDto;
import com.example.Utown.dto.optionDTO.OptionInfoDto;
import com.example.Utown.dto.dishDTO.DishForClientDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.DishMapper;
import com.example.Utown.mapper.FileInfoMapper;
import com.example.Utown.model.Dish;
import com.example.Utown.model.DishCategory;
import com.example.Utown.model.Element;
import com.example.Utown.model.FileInfo;
import com.example.Utown.model.Option;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.repository.DishRepository;
import com.example.Utown.repository.DishCategoryRepository;
import com.example.Utown.repository.ElementRepository;
import com.example.Utown.repository.OptionRepository;
import com.example.Utown.repository.RestaurantRepository;
import com.example.Utown.service.S3Service.FileInfoService;
import com.example.Utown.service.UserTypeService.RestaurantAdminService;
import com.example.Utown.service.UserTypeService.RestaurantAdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final AwsProperties awsProperties;
    private final DishCategoryRepository dishCategoryRepository;
    private final DishMapper dishMapper;
    private final ElementRepository elementRepository;
    private final FileInfoService fileInfoService;
    private final RestaurantAdminService restaurantAdminService;
    private final OptionRepository optionRepository;
    private final OptionService optionService;
    private final RestaurantRepository restaurantRepository;

    // ===== GET =====

    @Override
    public Dish getDishById(Long id) {
        return dishRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Dish", id));
    }

    @Override
    @Transactional(readOnly = true)
    public DishForClientDto getDishByIdForOrder(Long dishId) {
        Dish dish = getDishById(dishId);

        Set<Option> activeOptions = optionRepository.findByDishIdAndIsActiveTrue(dishId);

        for (Option option : activeOptions) {
            Set<Element> activeElements = elementRepository.findByOptionIdAndIsActiveTrueAndIsDeletedFalse(option.getId());
            option.setElements(activeElements);
        }

        dish.setOptions(activeOptions);

        return dishMapper.dishToClientDto(dish);
    }

    @Override
    public Page<DishDetailsDto> getDishesByRestaurantId(Long restaurantId, String title,
                                                        Integer sort, Long dishCategoryId,
                                                        Boolean isActive, int page, int size) {
        Page<Dish> dishPage = dishRepository.searchDishesByFilter(
                restaurantId,
                title,
                sort,
                dishCategoryId,
                isActive,
                PageRequest.of(page, size));

        return dishPage.map(dish -> {
            Set<OptionInfoDto> optionDtos = optionService.getOptionsWithElementsByDish(dish.getOptions());

            return new DishDetailsDto(
                    dish.getDescription(),
                    dish.getIsActive(),
                    dish.getPrice(),
                    dish.getSort(),
                    dish.getTitle(),
                    dish.getDishCategory().getId(),
                    optionDtos
            );
        });
    }

    @Override
    public List<DishSearchDto> getDishesByCategory(Long categoryId) {
        return dishRepository.findDishDtoByCategoryForClient(categoryId);
    }

    @Override
    public Page<DishSearchDto> searchDishesByRestaurant(Long restaurantId, String keyword, Pageable pageable) {
        return dishRepository.searchDishesByRestaurantAndKeyword(restaurantId, keyword, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DishMenuDto> getDishesByRestaurantWithFile(String categoryName) {
        Long restaurantId = restaurantAdminService.getCurrentAdmin().getRestaurant().getId();

        List<DishMenuDto> dishes = dishRepository.findActiveDishesByRestaurantIdAndCategoryName(restaurantId, categoryName);

        for (DishMenuDto dish : dishes) {
            if (dish.getFilePath() != null) {
                dish.setFileUrl(awsProperties.getPublicBaseUrl() + "/" + dish.getFilePath());
            }
        }
        return dishes;
    }

    @Transactional(readOnly = true)
    @Override
    public List<DishMenuDto> getInactiveDishesForRestaurant(String categoryName) {
        Long restaurantId = restaurantAdminService.getCurrentAdmin().getRestaurant().getId();
        List<DishMenuDto> dishes = dishRepository.findInactiveDishesByRestaurantIdAndCategoryName(restaurantId, categoryName);
        for (DishMenuDto dish : dishes) {
            if (dish.getFilePath() != null) {
                dish.setFileUrl(awsProperties.getPublicBaseUrl() + "/" + dish.getFilePath());
            }
        }
        return dishes;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DishDeletedMenuDto> getDeletedDishesForRestaurant(String categoryName) {
        Long restaurantId = restaurantAdminService.getCurrentAdmin().getRestaurant().getId();
        List<DishDeletedMenuDto> dishes = dishRepository.findDeletedDishesByRestaurantId(restaurantId, categoryName);
        for (DishDeletedMenuDto dish : dishes) {
            if (dish.getFilePath() != null) {
                dish.setFileUrl(awsProperties.getPublicBaseUrl() + "/" + dish.getFilePath());
            }
        }
        return dishes;
    }

    // ===== POST =====

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DishInfoDto createDishForRestaurant(Long restaurantId, DishCreateDto dto) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", restaurantId));

        DishCategory dishCategory = null;
        if (dto.getDishCategoryId() != null) {
            dishCategory = dishCategoryRepository.findById(dto.getDishCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("DishCategory", dto.getDishCategoryId()));
        }

        FileInfo file = null;
        if (dto.getFileId() != null) {
            file = fileInfoService.getFileInfoById(dto.getFileId());
        }

        Dish dish = Dish.builder()
                .description(dto.getDescription())
                .isActive(dto.getIsActive())
                .price(dto.getPrice())
                .title(dto.getTitle())
                .sort(dto.getSort())
                .dishCategory(dishCategory)
                .file(file)
                .restaurant(restaurant)
                .build();

        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
            Set<Option> options = optionService.createOptionsForDish(dish, dto.getOptions());
            dish.setOptions(options);
        }

        dishRepository.save(dish); // должно сохранить все каскадно

        return dishMapper.toSavedDishDto(dish);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DishInfoDto createDishAsRestaurantAdmin(DishCreateDto dto) {
        Long adminId = restaurantAdminService.getCurrentAdmin().getId();

        Restaurant restaurant = restaurantRepository.findByRestaurantAdminId(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant for admin", adminId));

        return createDishForRestaurant(restaurant.getId(), dto);
    }



    // ===== PUT =====

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public DishInfoDto updateDishForRestaurant(Long restaurantId, Long dishId, DishCreateDto dto) {
        Dish dish = getDishById(dishId);

        if (!dish.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException("Dish", dishId);
        }

        if (dto.getTitle() != null) {
            dish.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            dish.setDescription(dto.getDescription());
        }
        if (dto.getPrice() != null) {
            dish.setPrice(dto.getPrice());
        }
        if (dto.getSort() != null) {
            dish.setSort(dto.getSort());
        }
        if (dto.getIsActive() != null) {
            dish.setIsActive(dto.getIsActive());
        }

        if (dto.getDishCategoryId() != null) {
            DishCategory category = dishCategoryRepository.findById(dto.getDishCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("DishCategory", dto.getDishCategoryId()));
            dish.setDishCategory(category);
        }

        if (dto.getFileId() != null) {
            FileInfo file = fileInfoService.getFileInfoById(dto.getFileId());
            dish.setFile(file);
        }

        Set<Option> updatedOptions = optionService.updateOptionsForDish(dish, dto.getOptions());
        Set<Option> existingOptions = dish.getOptions();

        existingOptions.clear();
        existingOptions.addAll(updatedOptions);

        Dish savedDish = dishRepository.save(dish);
        return dishMapper.dishUpdateInfoToDto(savedDish);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public DishInfoDto updateDishAsRestaurantAdmin(Long dishId, DishCreateDto dto) {
        RestaurantAdmin admin = restaurantAdminService.getCurrentAdmin();
        Long restaurantId = admin.getRestaurant().getId();

        Dish dish = getDishById(dishId);
        if (!dish.getRestaurant().getId().equals(restaurantId)) {
            throw new AccessDeniedException("You do not have permission to update this dish.");
        }

        return updateDishForRestaurant(restaurantId, dishId, dto);
    }


    // ===== DELETE =====

    @Override
    public void deleteDish(Long id) {
        RestaurantAdmin currentAdmin = restaurantAdminService.getCurrentAdmin();
        Long adminRestaurantId = currentAdmin.getRestaurant().getId();
        Dish dish = getDishById(id);
        if (!dish.getRestaurant().getId().equals(adminRestaurantId)) {
            throw new AccessDeniedException("You do not have permission to delete this dish.");
        }
        dish.setIsDeleted(true);
        dishRepository.save(dish);
    }

}
