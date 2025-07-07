package com.example.Utown.service;

import com.example.Utown.dto.dishCategoryDTO.DishCategoryCreateDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryDetailsDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryDto;
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
    private final RestaurantRepository restaurantRepository;
    private final FileInfoRepository fileInfoRepository;

    @Override
    public DishCategoryDto createDishCategory(DishCategoryDto dto) {
        DishCategory entity = dishCategoryMapper.dishCategoryDtoToEntity(dto);

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
    public DishCategoryDto updateDishCategory(Long id, DishCategoryDto dto) {
        DishCategory entity = dishCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishCategory not found", id));

        entity.setName(dto.getName());
        entity.setSort(dto.getSort());
        entity.setIsActive(dto.getIsActive());

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

    @Override
    @Transactional
    public void deleteDishCategory(Long id) {
        DishCategory entity = dishCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishCategory not found", id));
        dishCategoryRepository.delete(entity);
    }

    @Override
    public Page<DishCategoryDetailsDto> getDishCategoriesByRestaurantId(Long restaurantId, int page, int size) { //метод для просмотра всех категорий Dish
        Pageable pageable = PageRequest.of(page, size, Sort.by("sort").ascending());
        return dishCategoryRepository.findByRestaurantId(restaurantId,pageable)
                .map(dishCategoryMapper::toDetailsDto);

    }
    @Override
    @Transactional
    public  DishCategoryDetailsDto createDishCategoryForRestaurant(Long restaurantId, DishCategoryCreateDto dto) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found", restaurantId));

        FileInfo file = null;
        if (dto.getFileId() != null) {
            file = fileInfoRepository.findById(dto.getFileId())
                    .orElseThrow(() -> new ResourceNotFoundException("File not found", dto.getFileId()));
        }
        DishCategory dishCategory = DishCategory.builder()
                .name(dto.getName())
                .sort(dto.getSort())
                .restaurant(restaurant)
                .file(file)
                .build();
        return dishCategoryMapper.toCreateDto(dishCategoryRepository.save(dishCategory));
    }

}




