package com.example.Utown.service;

import com.example.Utown.dto.dishCategoryDTO.DishCategoryDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.model.DishCategory;
import com.example.Utown.model.FileInfo;
import com.example.Utown.model.Restaurant;
import com.example.Utown.repository.DishCategoryRepository;
import com.example.Utown.repository.FileInfoRepository;
import com.example.Utown.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishCategoryServiceImpl implements DishCategoryService {

    private final DishCategoryRepository dishCategoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final FileInfoRepository fileInfoRepository;

    @Override
    @Transactional
    public DishCategory createDishCategory(DishCategoryDto dto) {
        Set<Restaurant> restaurants = new HashSet<>();
        if (dto.getRestaurants() != null) {
            for (Restaurant r : dto.getRestaurants()) {
                Restaurant restaurant = restaurantRepository.findById(r.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found", r.getId()));
                restaurants.add(restaurant);
            }
        }

        FileInfo fileInfo = null;
        if (dto.getFileId() != null) {
            fileInfo = fileInfoRepository.findById(dto.getFileId())
                    .orElseThrow(() -> new ResourceNotFoundException("FileInfo not found", dto.getFileId()));
        }

        DishCategory dishCategory = DishCategory.builder()
                .name(dto.getName())
                .sort(dto.getSort())
                .isActive(dto.getIsActive())
                .restaurants(restaurants)
                .file(fileInfo)
                .build();

        return dishCategoryRepository.save(dishCategory);
    }

    @Override
    @Transactional
    public DishCategoryDto getDishCategoryById(Long id) {
        DishCategory dishCategory = dishCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishCategory not found", id));

        dishCategory.getRestaurants().size();

        return mapToDto(dishCategory);
    }

    @Override
    @Transactional
    public List<DishCategoryDto> getAllDishCategories() {
        List<DishCategory> categories = dishCategoryRepository.findAll();

        // Инициализируем рестораны для каждого
        categories.forEach(cat -> cat.getRestaurants().size());

        return categories.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DishCategory updateDishCategory(Long id, DishCategoryDto dto) {
        DishCategory dishCategory = dishCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishCategory not found", id));

        Set<Restaurant> restaurants = new HashSet<>();
        if (dto.getRestaurants() != null) {
            for (Restaurant r : dto.getRestaurants()) {
                Restaurant restaurant = restaurantRepository.findById(r.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found", r.getId()));
                restaurants.add(restaurant);
            }
        }

        FileInfo fileInfo = null;
        if (dto.getFileId() != null) {
            fileInfo = fileInfoRepository.findById(dto.getFileId())
                    .orElseThrow(() -> new ResourceNotFoundException("FileInfo not found", dto.getFileId()));
        }

        dishCategory.setName(dto.getName());
        dishCategory.setSort(dto.getSort());
        dishCategory.setIsActive(dto.getIsActive());
        dishCategory.setRestaurants(restaurants);
        dishCategory.setFile(fileInfo);

        return dishCategoryRepository.save(dishCategory);
    }

    @Override
    @Transactional
    public void deleteDishCategory(Long id) {
        DishCategory dishCategory = dishCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishCategory not found", id));
        dishCategoryRepository.delete(dishCategory);
    }

    private DishCategoryDto mapToDto(DishCategory dishCategory) {
        return new DishCategoryDto(
                dishCategory.getId(),
                dishCategory.getName(),
                dishCategory.getSort(),
                dishCategory.getIsActive(),
                dishCategory.getCreatedAt(),
                dishCategory.getUpdatedAt(),
                dishCategory.getRestaurants(),
                dishCategory.getFile() != null ? dishCategory.getFile().getId() : null
        );
    }
}

