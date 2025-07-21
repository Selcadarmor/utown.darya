package com.example.Utown.service;

import com.example.Utown.dto.dishCategoryDTO.DishCategoryCreateDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryDetailsDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryRestaurantProfileDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.DishCategoryMapper;
import com.example.Utown.model.DishCategory;
import com.example.Utown.model.FileInfo;
import com.example.Utown.model.Restaurant;
import com.example.Utown.repository.DishCategoryRepository;
import com.example.Utown.repository.FileInfoRepository;
import com.example.Utown.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class DishCategoryServiceImpl implements DishCategoryService {

    private final DishCategoryRepository dishCategoryRepository;
    private final DishCategoryMapper dishCategoryMapper;
    private final RestaurantService restaurantService;
    private final FileInfoRepository fileInfoRepository;
    private final FileInfoService fileInfoService;
    private final RestaurantRepository restaurantRepository;

    // ===== GET =====

    @Override
    @Transactional
    public DishCategoryDto getDishCategoryById(Long id) {
        DishCategory entity = dishCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishCategory not found", id));
        return dishCategoryMapper.dishCategoryToDto(entity);
    }

    @Override
    public List<DishCategoryDto> getAllDishCategories() {
        return dishCategoryRepository.findAll()
                .stream()
                .map(dishCategoryMapper::dishCategoryToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DishCategoryDetailsDto> getDishCategoriesByRestaurantId(Long restaurantId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("sort").ascending());
        return dishCategoryRepository.findByRestaurantId(restaurantId, pageable)
                .map(dishCategoryMapper::toDetailsDto);
    }

    @Override
    public List<DishCategoryRestaurantProfileDto> getDishCategoriesByRestaurantForClient(Long restaurantId) {
        return dishCategoryRepository.findDishCategoriesWithDishCountByRestaurantId(restaurantId);
    }

    // ===== POST =====

    @Override
    public DishCategoryDto createDishCategory(DishCategoryDto dto) {
        DishCategory entity = dishCategoryMapper.dishCategoryDtoToEntity(dto);
        return getDishCategoryDto(dto, entity);
    }

    @Override
    @Transactional
    public DishCategoryDetailsDto createDishCategoryForRestaurant(Long restaurantId, DishCategoryCreateDto dto) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", restaurantId));

        FileInfo file = null;
        if (dto.getFileInfoId() != null) {
            file = fileInfoService.getFileInfoById(dto.getFileInfoId());
        }

        DishCategory dishCategory = DishCategory.builder()
                .name(dto.getName())
                .sort(dto.getSort())
                .restaurant(restaurant)
                .file(file)
                .build();

        return dishCategoryMapper.toCreateDto(dishCategoryRepository.save(dishCategory));
    }

    // ===== PUT =====

    @Override
    public DishCategoryDto updateDishCategory(Long id, DishCategoryDto dto) {
        DishCategory entity = dishCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishCategory", id));

        entity.setName(dto.getName());
        entity.setSort(dto.getSort());
        entity.setIsActive(dto.getIsActive());

        return getDishCategoryDto(dto, entity);
    }

    // ===== DELETE =====

    @Override
    @Transactional
    public void deleteDishCategory(Long id) {
        DishCategory entity = dishCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishCategory not found", id));
        dishCategoryRepository.delete(entity);
    }

    // ===== PRIVATE =====

    private DishCategoryDto getDishCategoryDto(DishCategoryDto dto, DishCategory entity) {
        if (dto.getRestaurantId() != null) {
            Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found", dto.getRestaurantId()));
            entity.setRestaurant(restaurant);
        }

        if (dto.getFile() != null) {
            Long fileId = dto.getFile().getId();
            FileInfo file = fileInfoRepository.findById(fileId)
                    .orElseThrow(() -> new ResourceNotFoundException("File not found", fileId));
            entity.setFile(file);
        }

        DishCategory saved = dishCategoryRepository.save(entity);
        return dishCategoryMapper.dishCategoryToDto(saved);
    }
}
