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
import com.example.Utown.model.FileInfo;
import com.example.Utown.repository.FileInfoRepository;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantCategoryServiceImpl implements RestaurantCategoryService {

    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final RestaurantCategoryMapper restaurantCategoryMapper;
    private final FileInfoRepository fileInfoRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public RestaurantCategory createRestaurantCategory(RestaurantCategoryDto dto) {
        RestaurantCategory entity = restaurantCategoryMapper.restaurantCategoryDtoToEntity(dto);

        if (dto.getFile() != null) {
            Long fileId = dto.getFile().getId();
            FileInfo file = fileInfoRepository.findById(fileId)
                    .orElseThrow(() -> new ResourceNotFoundException("File not found", fileId));
            entity.setFile(file);
        }

        return restaurantCategoryRepository.save(entity);
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
    public List<RestaurantCategoryForClient> getAllCategoriesWithCount() {
        List<RestaurantCategory> categories = restaurantCategoryRepository.findAll();

        return categories.stream()
                .map(category -> {
                    Long count = restaurantRepository.countByCategory(category); // или другой корректный метод
                    return new RestaurantCategoryForClient(
                            category.getId(),
                            category.getName(),
                            category.getFile(),
                            count
                    );
                })
                .toList();
    }

}

