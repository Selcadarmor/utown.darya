package com.example.Utown.service;

import com.example.Utown.dto.dishCategoryDTO.DishCategoryCreateDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryCreateResponseDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryDetailsDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryRestaurantProfileDto;
import com.example.Utown.exception.InvalidOperationException;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.DishCategoryMapper;
import com.example.Utown.model.Dish;
import com.example.Utown.model.DishCategory;
import com.example.Utown.model.FileInfo;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.repository.DishCategoryRepository;
import com.example.Utown.repository.DishRepository;
import com.example.Utown.service.S3Service.FileInfoService;
import com.example.Utown.service.UserTypeService.RestaurantAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DishCategoryServiceImpl implements DishCategoryService {

    private final DishCategoryRepository dishCategoryRepository;
    private final DishCategoryMapper dishCategoryMapper;
    private final DishRepository dishRepository;
    private final FileInfoService fileInfoService;
    private final RestaurantService restaurantService;
    private final RestaurantAdminService restaurantAdminService;

    // ===== GET =====

    @Override
    @Transactional
    public DishCategory getDishCategoryById(Long id) {
        log.info("DishCategoryServiceImpl.getDishCategoryById: id={}", id);

        return dishCategoryRepository.findById(id)
                .orElseThrow(() ->  {
                    log.error("DishCategoryServiceImpl.getDishCategoryById: DishCategory not found by id={}", id);
                    return new ResourceNotFoundException("DishCategory", id);
                });
    }

    @Override
    public List<DishCategory> getAllDishCategories() {
        log.info("DishCategoryServiceImpl.getAllDishCategories");
        return dishCategoryRepository.findAll();
    }

    @Override
    public Page<DishCategoryDetailsDto> getDishCategoriesByRestaurantId(
            Long restaurantId, String query, Integer sort, Boolean isActive, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("sort").ascending());
        log.info("Getting dish categories for restaurantId: {}", restaurantId);
        log.debug("Filters -> query: '{}', sort: {}, isActive: {}, page: {}, size: {}",
                query, sort, isActive, page, size);
        return dishCategoryRepository.findAllWithFilter(restaurantId, query, sort, isActive, pageable)
                .map(dishCategoryMapper::toDetailsDto);
    }

    @Override
    public List<DishCategoryRestaurantProfileDto> getDishCategoriesByRestaurantForClient(Long restaurantId) {
        log.info("Getting dish categories for restaurantId: {}", restaurantId);
        return dishCategoryRepository.findDishCategoriesWithDishCountByRestaurantId(restaurantId);
    }

    @Override
    public List<DishCategoryDto> getCategoriesByRestaurant() {
        RestaurantAdmin currentAdmin = restaurantAdminService.getCurrentAdmin();
        Long restaurantId = currentAdmin.getRestaurant().getId();

        log.info("Getting dish categories for restaurantId: {}", restaurantId);

        List<DishCategory> categories = dishCategoryRepository.findAllByRestaurantId(restaurantId);

        log.info("Found {} categories for restaurantId: {}", categories.size(), restaurantId);
        return categories.stream()
                .map(dishCategoryMapper::dishCategoryToDto)
                .toList();
    }
    // ===== POST =====

    @Override
    @Transactional
    public DishCategoryCreateResponseDto createDishCategoryForRestaurant(Long restaurantId, DishCategoryCreateDto dto) {
        log.info("Creating dish category for restaurantId={}, name={}", restaurantId, dto.getName());
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);

        FileInfo file = null;
        if (dto.getFileId() != null) {
            log.debug("Fetching file info with id={}", dto.getFileId());
            file = fileInfoService.getFileInfoById(dto.getFileId());
        }

        DishCategory dishCategory = DishCategory.builder()
                .name(dto.getName())
                .sort(dto.getSort())
                .isActive(true)
                .restaurant(restaurant)
                .file(file)
                .build();
        log.debug("Constructed DishCategory: {}", dishCategory);

        return dishCategoryMapper.toCreateDto(dishCategoryRepository.save(dishCategory));
    }

    @Override
    @Transactional
    public DishCategoryCreateResponseDto createDishCategoryForRestaurantByAdmin(DishCategoryCreateDto dto) {
        RestaurantAdmin currentAdmin = restaurantAdminService.getCurrentAdmin();
        Long restaurantId = currentAdmin.getRestaurant().getId();
        log.info("Creating dish category: name={}, restaurantId={}", dto.getName(), restaurantId);

        return createDishCategoryForRestaurant(restaurantId, dto); // вызов уже имеющегося метода
    }


    // ===== PUT =====

    @Override
    @Transactional
    public DishCategoryDto updateDishCategory(Long id, DishCategoryDto dto) {
        log.info("Updating dish category: id={}, name={}", id, dto.getName());
        DishCategory category = getDishCategoryById(id);

        category.setName(dto.getName());
        category.setSort(dto.getSort());
        category.setIsActive(dto.getIsActive());

        if (dto.getFileId() != null) {
            log.debug("Updating file info with id={}", dto.getFileId());
            FileInfo file = fileInfoService.getFileInfoById(dto.getFileId());
            category.setFile(file);
        } else {
            log.debug("Removing file from category with id={}", id);
            category.setFile(null);
        }
        category = dishCategoryRepository.save(category);
        log.debug("Updated dish category: {}", category);
        return dishCategoryMapper.dishCategoryToDto(category);
    } //проверить посмотреть возможно ли сейчас что б файл был нал если нет сделать

    // ===== DELETE =====

    @Override
    @Transactional
    public void deleteDishCategory(Long id) {
        log.info("Deleting dish category: id={}", id);
        DishCategory category= getDishCategoryById(id);
        DishCategory defaultCategory = dishCategoryRepository.findByName("No category")
                .orElseThrow(() -> {
                    log.error("DishCategoryServiceImpl.deleteDishCategory: Default category 'No category' not found");
                    return new ResourceNotFoundException("DishCategory", "No category");
                });

        // Если пытаемся удалить эту категорию — запрещаем
        if (category.getId().equals(defaultCategory.getId())) {
            log.error("Attempt to delete default category 'No category'. categoryId={}, restaurantId={}", category.getId(), category.getRestaurant().getId());
            throw new InvalidOperationException("You cannot delete the default 'No category' category.");
        }

        // Перевязываем все блюда на "Без категории"
        List<Dish> dishes = dishRepository.findByDishCategoryId(category.getId());
        for (Dish dish : dishes) {
            dish.setDishCategory(defaultCategory);
            dishRepository.save(dish);
            log.debug("Reassigned dish id={} to default category", dish.getId());
        }

        // Мягкое удаление категории (например, ставим inactive)
        category.setIsActive(false);
        dishCategoryRepository.save(category);
        log.info("Successfully deleted dish category: id={}, name={}", category.getId(), category.getName());
    }

}
