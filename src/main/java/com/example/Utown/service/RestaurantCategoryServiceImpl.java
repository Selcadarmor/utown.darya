package com.example.Utown.service;

import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryForClient;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.RestaurantCategoryMapper;
import com.example.Utown.model.RestaurantCategory;
import com.example.Utown.repository.RestaurantCategoryRepository;
import com.example.Utown.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantCategoryServiceImpl implements RestaurantCategoryService {

    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantCategoryMapper restaurantCategoryMapper;

    @Override
    public RestaurantCategoryDto createCategory(RestaurantCategoryDto dto) {
        RestaurantCategory entity = restaurantCategoryMapper.restaurantCategoryDtoToEntity(dto);
        RestaurantCategory saved = restaurantCategoryRepository.save(entity);
        return restaurantCategoryMapper.restaurantCategoryToDto(saved);
    }

    @Override
    public RestaurantCategoryDto getCategoryById(Long id) {
        RestaurantCategory category = restaurantCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RestaurantCategory not found", id));
        return restaurantCategoryMapper.restaurantCategoryToDto(category);
    }

    @Override
    public List<RestaurantCategoryDto> getAllCategories() {
        return restaurantCategoryRepository.findAll()
                .stream()
                .map(restaurantCategoryMapper::restaurantCategoryToDto)
                .toList();
    }

    @Override
    public RestaurantCategoryDto updateCategory(Long id, RestaurantCategoryDto dto) {
        RestaurantCategory existing = restaurantCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RestaurantCategory not found", id));
        existing.setImageUrl(dto.getImageUrl());
        existing.setName(dto.getName());
        existing.setSort(dto.getSort());
        existing.setIsActive(dto.getIsActive());
        RestaurantCategory updated = restaurantCategoryRepository.save(existing);
        return restaurantCategoryMapper.restaurantCategoryToDto(updated);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!restaurantCategoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("RestaurantCategory not found", id);
        }
        restaurantCategoryRepository.deleteById(id);
    }

    @Override
    public List<RestaurantCategoryForClient> getAllCategoriesWithCount() {
        List<RestaurantCategory> categories = restaurantCategoryRepository.findAll();

        return categories.stream()
                .map(category -> {
                    Long count = restaurantRepository.countByCategory(category);
                    return new RestaurantCategoryForClient(
                            category.getId(),
                            category.getName(),
                            category.getImageUrl(),
                            count
                    );
                })
                .toList();
    }
}

