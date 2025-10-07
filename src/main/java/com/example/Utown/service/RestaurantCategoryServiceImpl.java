package com.example.Utown.service;

import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryCreateDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryForClient;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.RestaurantCategoryMapper;
import com.example.Utown.model.RestaurantCategory;
import com.example.Utown.repository.RestaurantCategoryRepository;
import com.example.Utown.repository.RestaurantRepository;
import com.example.Utown.service.S3Service.FileInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import com.example.Utown.model.FileInfo;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantCategoryServiceImpl implements RestaurantCategoryService {

    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final FileInfoService fileInfoService;
    private final RestaurantCategoryMapper restaurantCategoryMapper;
    private final RestaurantRepository restaurantRepository;

    // ===== GET =====

    @Override
    @Transactional(readOnly = true)
    public RestaurantCategory getRestaurantCategoryById(Long id) {
        log.info("RestaurantCategoryServiceImpl.getRestaurantCategoryById: id={}", id);
        return restaurantCategoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("RestaurantCategoryServiceImpl.getRestaurantCategoryById: RestaurantCategory not found by id={}", id);
                    return new ResourceNotFoundException("RestaurantCategory", id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantCategory> getAllRestaurantCategories() {
        log.info("RestaurantCategoryServiceImpl.getAllRestaurantCategories");
        return restaurantCategoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantCategoryForClient> getAllCategoriesWithRestaurantCount() {
        log.info("RestaurantCategoryServiceImpl.getAllCategoriesWithRestaurantCount");
        List<RestaurantCategory> categories = restaurantCategoryRepository.findAllActiveRestaurantCategories();

        return categories.stream()
                .map(category -> {
                    String filePath = category.getFile() != null ? category.getFile().getPath() : null;
                    Long count = restaurantRepository.countRestaurantsByCategory(category.getId());
                    log.debug("Found {} restaurants for category id={}", count, category.getId());

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
        log.info("Creating category: name={}", dto.getName());
        RestaurantCategory category = restaurantCategoryMapper.toEntity(dto);
        category.setIsActive(true);

        if (dto.getFileId() != null) {
            log.debug("Fetching file info with id={}", dto.getFileId());
            FileInfo fileInfo = fileInfoService.getFileInfoById(dto.getFileId());
            category.setFile(fileInfo);
        }

        RestaurantCategory saved = restaurantCategoryRepository.save(category);
        log.debug("Created category: {}", saved);
        return restaurantCategoryMapper.toDto(saved);
    }


    // ===== PUT =====
    @Override//обнавление для категорий ресторана
    @Transactional(rollbackFor = RuntimeException.class)
    public RestaurantCategoryDto updateRestaurantCategory(Long id, RestaurantCategoryCreateDto dto) {
        log.info("Updating category: id={}, name={}", id, dto.getName());
        RestaurantCategory category = getRestaurantCategoryById(id);
        restaurantCategoryMapper.updateFromDto(dto, category);
        category.setIsActive(true);

        if (dto.getFileId() != null) {
            log.debug("Updating file info with id={}", dto.getFileId());
            FileInfo fileInfo = fileInfoService.getFileInfoById(dto.getFileId());
            category.setFile(fileInfo);
        } else {
            log.debug("Removing file from category with id={}", id);
            category.setFile(null);
        }

        RestaurantCategory updated = restaurantCategoryRepository.save(category);
        log.debug("Updated category: {}", updated);
        return restaurantCategoryMapper.toDto(updated);
    }

    // ===== DELETE =====

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteRestaurantCategory(Long id) {
        log.info("Deleting category: id={}", id);
        RestaurantCategory category = getRestaurantCategoryById(id);
        category.setIsActive(false);
        log.debug("Category with id={} is now inactive", category.getId());
        restaurantCategoryRepository.save(category);
    }

}
