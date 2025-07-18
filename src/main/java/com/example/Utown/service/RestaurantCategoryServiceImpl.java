package com.example.Utown.service;

import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryDto;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryForClient;
import com.example.Utown.dto.restaurantCategoryDTO.RestaurantCategoryInfoDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.RestaurantCategoryInfoMapper;
import com.example.Utown.mapper.RestaurantCategoryMapper;
import com.example.Utown.model.RestaurantCategory;
import com.example.Utown.repository.RestaurantCategoryRepository;
import com.example.Utown.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.example.Utown.model.FileInfo;
import com.example.Utown.repository.FileInfoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantCategoryServiceImpl implements RestaurantCategoryService {

    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final RestaurantCategoryMapper restaurantCategoryMapper;
    private final FileInfoRepository fileInfoRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantCategoryInfoMapper restaurantCategoryInfoMapper;

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public List<RestaurantCategory> createRestaurantCategories(List<RestaurantCategoryDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }

        List<RestaurantCategory> categories = new ArrayList<>();

        for (RestaurantCategoryDto dto : dtos) {
            RestaurantCategory entity;
            if (dto.getId() != null) {
                // Загрузка из базы для обновления существующей категории
                entity = restaurantCategoryRepository.findById(dto.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found", dto.getId()));
                // Обновляем поля entity на основании dto (например через маппер)
                restaurantCategoryInfoMapper.updateEntityFromDto(dto, entity);
            } else {
                // Новая категория
                entity = restaurantCategoryMapper.restaurantCategoryDtoToEntity(dto);
            }

            if (dto.getFile() != null) {
                Long fileId = dto.getFile().getId();
                FileInfo file = fileInfoRepository.findById(fileId)
                        .orElseThrow(() -> new ResourceNotFoundException("File not found", fileId));
                entity.setFile(file);
            }

            categories.add(entity);
        }

        return restaurantCategoryRepository.saveAll(categories);
    }




    @Override
    public RestaurantCategoryDto getRestaurantCategoryById(Long id) {
        return restaurantCategoryRepository.findById(id)
                .map(restaurantCategoryMapper::restaurantCategoryToDto)
                .orElseThrow(() -> new ResourceNotFoundException("RestaurantCategory not found", id));
    }

    @Override
    public List<RestaurantCategoryDto> getAllRestaurantCategories() {
        return restaurantCategoryRepository.findAll()
                .stream()
                .map(restaurantCategoryMapper::restaurantCategoryToDto)
                .collect(Collectors.toList());
    }
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public RestaurantCategory updateRestaurantCategory(Long id, RestaurantCategoryDto dto) {
        RestaurantCategory category = restaurantCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RestaurantCategory not found", id));

        category.setName(dto.getName());
        category.setSort(dto.getSort());
        category.setIsActive(dto.getIsActive());

        if (dto.getFile() != null) {
            Long fileId = dto.getFile().getId();
            FileInfo file = fileInfoRepository.findById(fileId)
                    .orElseThrow(() -> new ResourceNotFoundException("File not found", fileId));
            category.setFile(file);
        }

        return restaurantCategoryRepository.save(category);
    }

    @Override
    public void deleteRestaurantCategory(Long id) {
        RestaurantCategory category = restaurantCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RestaurantCategory not found", id));
        restaurantCategoryRepository.delete(category);
    }

    @Override
    public List<RestaurantCategoryForClient> getAllCategoriesWithRestaurantCount() {
        List<RestaurantCategory> categories = restaurantCategoryRepository.findAllActiveRestaurantCategories();

        return categories.stream()
                .map(category -> {
                    String filePath = category.getFile() != null ? category.getFile().getPath() : null;

                    Long count = restaurantRepository.countRestaurantsByCategory(category);

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

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RestaurantCategory updateRestaurantCategoryForRestaurant(Long id, RestaurantCategoryInfoDto dto) {
        RestaurantCategory restaurantCategory = restaurantCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RestaurantCategory not found", id));
        restaurantCategory.setName(dto.getName());
        return  restaurantCategoryRepository.save(restaurantCategory);
    }

}

