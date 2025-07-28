package com.example.Utown.service;

import com.example.Utown.dto.dishCategoryDTO.DishCategoryCreateDto;
import com.example.Utown.dto.dishCategoryDTO.DishCategoryCreateResponseDto;
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
import com.example.Utown.service.S3Service.FileInfoService;
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
    private final FileInfoRepository fileInfoRepository;
    private final FileInfoService fileInfoService;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;

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

    // ===== PUT =====

    @Override
    public DishCategoryDto updateDishCategory(Long id, DishCategoryDto dto) {
        DishCategory entity = getDishCategory(id);

        entity.setName(dto.getName());
        entity.setSort(dto.getSort());
        entity.setIsActive(dto.getIsActive());

        //добавить fileInfo
        return getDishCategoryDto(dto, entity);
    }

    // ===== DELETE =====

    @Override
    @Transactional
    public void deleteDishCategory(Long id) {
        DishCategory entity = getDishCategory(id);
        dishCategoryRepository.delete(entity);
    } //поменять на isActive = False

    private DishCategory getDishCategory(Long id) {
        return dishCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishCategory", id));
    }

    // ===== PRIVATE =====

    private DishCategoryDto getDishCategoryDto(DishCategoryDto dto, DishCategory entity) {
        if (dto.getRestaurantId() != null) {
            Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Restaurant", dto.getRestaurantId()));
            entity.setRestaurant(restaurant);
        }

        if (dto.getFile() != null) {
            Long fileId = dto.getFile().getId();
            FileInfo file = fileInfoRepository.findById(fileId)
                    .orElseThrow(() -> new ResourceNotFoundException("File", fileId));
            entity.setFile(file);
        }

        DishCategory saved = dishCategoryRepository.save(entity);
        return dishCategoryMapper.dishCategoryToDto(saved);
    }
}
