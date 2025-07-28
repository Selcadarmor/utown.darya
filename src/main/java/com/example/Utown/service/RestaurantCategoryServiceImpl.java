package com.example.Utown.service;

import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryCreateDto;
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
import com.example.Utown.model.FileInfo;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantCategoryServiceImpl implements RestaurantCategoryService {

    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final RestaurantCategoryMapper restaurantCategoryMapper;
    private final RestaurantRepository restaurantRepository;
    private final FileInfoService fileInfoService;

    // ===== GET =====

    @Override
    @Transactional(readOnly = true)
    public RestaurantCategory getRestaurantCategoryById(Long id) {
        return getRestaurantCategory(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantCategory> getAllRestaurantCategories() {
        return restaurantCategoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantCategoryForClient> getAllCategoriesWithRestaurantCount() {
        List<RestaurantCategory> categories = restaurantCategoryRepository.findAllActiveRestaurantCategories();

        return categories.stream()
                .map(category -> {
                    String filePath = category.getFile() != null ? category.getFile().getPath() : null;
                    Long count = restaurantRepository.countRestaurantsByCategory(category.getId());

                    return new RestaurantCategoryForClient(
                            category.getId(),
                            category.getName(),
                            category.getSort(),
                            category.getIsActive(),
                            filePath,
                            count
                    );
                })
                .collect(Collectors.toList());
    }


    // ===== POST =====

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RestaurantCategoryDto createCategory(RestaurantCategoryCreateDto dto) {
        RestaurantCategory category = restaurantCategoryMapper.toEntity(dto);

        if (dto.getFileId() != null) {
            FileInfo fileInfo = fileInfoService.getFileInfoById(dto.getFileId());
            category.setFile(fileInfo);
        }

        RestaurantCategory saved = restaurantCategoryRepository.save(category);
        return restaurantCategoryMapper.toDto(saved);
    }


    // ===== PUT =====
    @Override//обнавление для категорий ресторана
    @Transactional(rollbackFor = RuntimeException.class)
    public RestaurantCategoryDto updateRestaurantCategory(Long id, RestaurantCategoryCreateDto dto) {
        RestaurantCategory category = getRestaurantCategory(id);
        restaurantCategoryMapper.updateFromDto(dto, category);

        if (dto.getFileId() != null) {
            FileInfo fileInfo = fileInfoService.getFileInfoById(dto.getFileId());
            category.setFile(fileInfo);
        } else {
            category.setFile(null);
        }

        RestaurantCategory updated = restaurantCategoryRepository.save(category);
        return restaurantCategoryMapper.toDto(updated);
    }

    // ===== DELETE =====

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteRestaurantCategory(Long id) {
        RestaurantCategory category = getRestaurantCategory(id);
        restaurantCategoryRepository.delete(category);
    }

    // ===== PRIVATE =====
    private RestaurantCategory getRestaurantCategory(Long id) {
        return restaurantCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RestaurantCategory", id));
    }
}
