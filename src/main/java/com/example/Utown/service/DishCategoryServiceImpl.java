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
import com.example.Utown.service.UserTypeService.RestaurantAdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
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
        return dishCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishCategory", id));
    }

    @Override
    public List<DishCategory> getAllDishCategories() {
        return dishCategoryRepository.findAll();
    }

    @Override
    public Page<DishCategoryDetailsDto> getDishCategoriesByRestaurantId(
            Long restaurantId, String query, Integer sort, Boolean isActive, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("sort").ascending());
        return dishCategoryRepository.findAllWithFilter(restaurantId, query, sort, isActive, pageable)
                .map(dishCategoryMapper::toDetailsDto);
    }

    @Override
    public List<DishCategoryRestaurantProfileDto> getDishCategoriesByRestaurantForClient(Long restaurantId) {
        return dishCategoryRepository.findDishCategoriesWithDishCountByRestaurantId(restaurantId);
    }

    @Override
    public List<DishCategoryDto> getCategoriesByRestaurant() {
        RestaurantAdmin currentAdmin = restaurantAdminService.getCurrentAdmin();
        Long restaurantId = currentAdmin.getRestaurant().getId();

        List<DishCategory> categories = dishCategoryRepository.findAllByRestaurantId(restaurantId);
        return categories.stream()
                .map(dishCategoryMapper::dishCategoryToDto)
                .toList();
    }
    // ===== POST =====

    @Override
    @Transactional
    public DishCategoryCreateResponseDto createDishCategoryForRestaurant(Long restaurantId, DishCategoryCreateDto dto) {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);

        FileInfo file = null;
        if (dto.getFileId() != null) {
            file = fileInfoService.getFileInfoById(dto.getFileId());
        }

        DishCategory dishCategory = DishCategory.builder()
                .name(dto.getName())
                .sort(dto.getSort())
                .isActive(true)
                .restaurant(restaurant)
                .file(file)
                .build();

        return dishCategoryMapper.toCreateDto(dishCategoryRepository.save(dishCategory));
    }

    @Override
    @Transactional
    public DishCategoryCreateResponseDto createDishCategoryForRestaurantByAdmin(DishCategoryCreateDto dto) {
        RestaurantAdmin currentAdmin = restaurantAdminService.getCurrentAdmin();
        Long restaurantId = currentAdmin.getRestaurant().getId();

        return createDishCategoryForRestaurant(restaurantId, dto); // вызов уже имеющегося метода
    }


    // ===== PUT =====

    @Override
    public DishCategoryDto updateDishCategory(Long id, DishCategoryDto dto) {
        DishCategory category = getDishCategoryById(id);

        category.setName(dto.getName());
        category.setSort(dto.getSort());
        category.setIsActive(dto.getIsActive());

        if (dto.getFileId() != null) {
            FileInfo file = fileInfoService.getFileInfoById(dto.getFileId());
            category.setFile(file);
        }
        category = dishCategoryRepository.save(category);
        return dishCategoryMapper.dishCategoryToDto(category);
    }

    // ===== DELETE =====

    @Override
    @Transactional
    public void deleteDishCategory(Long id) {
        DishCategory category= getDishCategoryById(id);
        DishCategory defaultCategory = dishCategoryRepository.findByName("No category")
                .orElseThrow(() -> new IllegalStateException("Default category 'No category' is missing"));

        // Если пытаемся удалить эту категорию — запрещаем
        if (category.getId().equals(defaultCategory.getId())) {
            throw new InvalidOperationException("You cannot delete the default 'No category' category.");
        }

        // Перевязываем все блюда на "Без категории"
        List<Dish> dishes = dishRepository.findByDishCategoryId(category.getId());
        for (Dish dish : dishes) {
            dish.setDishCategory(defaultCategory);
            dishRepository.save(dish);
        }

        // Мягкое удаление категории (например, ставим inactive)
        category.setIsActive(false);
        dishCategoryRepository.save(category);
    }


}
