package com.example.Utown.service;

import com.example.Utown.dto.dishCategoryDTO.DishCategoryCreateDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryCreateResponseDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryDetailsDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryRestaurantProfileDto;
import com.example.Utown.exception.InvalidOperationException;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.DishCategoryMapper;
import com.example.Utown.model.DishCategory;
import com.example.Utown.model.FileInfo;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.repository.DishCategoryRepository;
import com.example.Utown.repository.DishRepository;
import com.example.Utown.repository.FileInfoRepository;
import com.example.Utown.repository.RestaurantRepository;
import com.example.Utown.service.S3Service.FileInfoService;
import com.example.Utown.service.UserTypeService.RestaurantAdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@RequiredArgsConstructor
public class DishCategoryServiceImpl implements DishCategoryService {

    private final DishCategoryRepository dishCategoryRepository;
    private final DishCategoryMapper dishCategoryMapper;
    private final FileInfoRepository fileInfoRepository;
    private final FileInfoService fileInfoService;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;
    private final DishRepository dishRepository;
    private final RestaurantAdminServiceImpl restaurantAdminService;

    // ===== GET =====

    @Override
    @Transactional
    public DishCategory getDishCategoryById(Long id) {
        DishCategory dishCategory = getDishCategory(id);
        return dishCategory;
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
        DishCategory category = getDishCategory(id);

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
        DishCategory category= getDishCategory(id);
        boolean hasDishes = dishRepository.existsByDishCategoryId(id);

        if (hasDishes) {
            throw new InvalidOperationException("You cannot delete a category that dishes are linked to.");
        }
        category.setIsActive(false);
        dishCategoryRepository.save(category);
    }

    private DishCategory getDishCategory(Long id) {
        return dishCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishCategory", id));
    }


}
