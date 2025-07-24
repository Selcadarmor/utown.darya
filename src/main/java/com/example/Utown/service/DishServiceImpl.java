package com.example.Utown.service;

import com.example.Utown.dto.dishDTO.DishCreateDto;
import com.example.Utown.dto.dishDTO.DishDetailsDto;
import com.example.Utown.dto.dishDTO.DishDto;
import com.example.Utown.dto.dishDTO.DishInfoDto;
import com.example.Utown.dto.dishDTO.DishSearchDto;
import com.example.Utown.dto.elementDTO.ElementInfoDto;
import com.example.Utown.dto.optionDTO.OptionInfoDto;
import com.example.Utown.dto.dishDTO.DishForClientDto;
import com.example.Utown.dto.elementDTO.ElementForClientDto;
import com.example.Utown.dto.optionDTO.OptionForClientDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.DishMapper;
import com.example.Utown.model.Dish;
import com.example.Utown.model.DishCategory;
import com.example.Utown.model.Element;
import com.example.Utown.model.FileInfo;
import com.example.Utown.model.Option;
import com.example.Utown.model.Restaurant;
import com.example.Utown.repository.DishRepository;
import com.example.Utown.repository.DishCategoryRepository;
import com.example.Utown.repository.ElementRepository;
import com.example.Utown.repository.FileInfoRepository;
import com.example.Utown.repository.OptionRepository;
import com.example.Utown.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishCategoryRepository dishCategoryRepository;
    private final FileInfoRepository fileInfoRepository;
    private final DishMapper dishMapper;
    private final OptionRepository optionRepository;
    private final OptionService optionService;
    private final ElementRepository elementRepository;

    // ===== GET =====

    @Override
    public DishDto getDishById(Long id) {
        return dishRepository.findDishById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found", id));
    }

    @Override
    public Page<DishDetailsDto> getDishesByRestaurantId(Long restaurantId, String title,
                                                        Integer sort, Long dishCategoryId,
                                                        Boolean isActive, int page, int size) {
        Page<Dish> dishPage = dishRepository.searchDishesByFilter(
                restaurantId,
                title,
                sort,
                dishCategoryId,
                isActive,
                PageRequest.of(page, size));

        return dishPage.map(dish -> {
            Set<OptionInfoDto> optionDtos = optionService.getOptionsWithElementsByDish(dish.getOptions());

            return new DishDetailsDto(
                    dish.getDescription(),
                    dish.getIsActive(),
                    dish.getPrice(),
                    dish.getSort(),
                    dish.getTitle(),
                    dish.getDishCategory().getId(),
                    optionDtos
            );
        });
    }

    @Override
    @Transactional(readOnly = true)
    public DishForClientDto getDishByIdForClient(Long dishId) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new ResourceNotFoundException("Dish", dishId));

        Set<Option> activeOptions = optionRepository.findByDishIdAndIsActiveTrue(dishId);

        for (Option option : activeOptions) {
            Set<Element> activeElements = elementRepository.findByOptionIdAndIsActiveTrueAndIsDeletedFalse(option.getId());
            option.setElements(activeElements);
        }

        dish.setOptions(activeOptions);

        return dishMapper.dishToClientDto(dish);
    }

    @Override
    public List<DishSearchDto> getDishesByCategoryForClient(Long categoryId) {
        return dishRepository.findDishDtoByCategoryForClient(categoryId);
    }

    @Override
    public Page<DishSearchDto> searchDishesByRestaurant(Long restaurantId, String keyword, Pageable pageable) {
        return dishRepository.searchDishesByRestaurantAndKeyword(restaurantId, keyword, pageable);
    }

    // ===== POST =====

    @Override
    public Dish createDish(DishDto dto) {
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found", dto.getRestaurantId()));

        DishCategory dishCategory = dishCategoryRepository.findById(dto.getDishCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("DishCategory not found", dto.getDishCategoryId()));

        FileInfo fileInfo = fileInfoRepository.findById(dto.getFileId())
                .orElseThrow(() -> new ResourceNotFoundException("FileInfo not found", dto.getFileId()));

        Dish dish = Dish.builder()
                .description(dto.getDescription())
                .isActive(dto.getIsActive())
                .isDeleted(dto.getIsDeleted())
                .price(dto.getPrice())
                .sort(dto.getSort())
                .title(dto.getTitle())
                .restaurant(restaurant)
                .dishCategory(dishCategory)
                .file(fileInfo)
                .build();

        if (dto.getOptions() != null) {
            dto.getOptions().forEach(option -> option.setDish(dish));
            dish.setOptions(dto.getOptions());
        }

        return dishRepository.save(dish);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DishInfoDto createDishForRestaurant(Long restaurantId, DishCreateDto dto) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", restaurantId));

        DishCategory dishCategory = null;
        if (dto.getDishCategoryId() != null) {
            dishCategory = dishCategoryRepository.findById(dto.getDishCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("DishCategory", dto.getDishCategoryId()));
        }

        FileInfo file = null;
        if (dto.getFileId() != null) {
            file = fileInfoRepository.findById(dto.getFileId()).orElse(null);
        }

        Dish dish = Dish.builder()
                .description(dto.getDescription())
                .isActive(dto.getIsActive())
                .price(dto.getPrice())
                .title(dto.getTitle())
                .sort(dto.getSort())
                .dishCategory(dishCategory)
                .file(file)
                .restaurant(restaurant)
                .build();

        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
            Set<Option> options = optionService.createOptionsForDish(dish, dto.getOptions());
            dish.setOptions(options);
        }

        dishRepository.save(dish); // должно сохранить все каскадно

        return dishMapper.toSavedDishDto(dish);
    }


    // ===== PUT =====

    @Override
    public Dish updateDish(Long id, DishDto dto) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish", id));

        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", dto.getRestaurantId()));

        DishCategory dishCategory = dishCategoryRepository.findById(dto.getDishCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("DishCategory", dto.getDishCategoryId()));

        FileInfo fileInfo = fileInfoRepository.findById(dto.getFileId())
                .orElseThrow(() -> new ResourceNotFoundException("FileInfo", dto.getFileId()));

        dish.setDescription(dto.getDescription());
        dish.setIsActive(dto.getIsActive());
        dish.setIsDeleted(dto.getIsDeleted());
        dish.setPrice(dto.getPrice());
        dish.setSort(dto.getSort());
        dish.setTitle(dto.getTitle());
        dish.setRestaurant(restaurant);
        dish.setDishCategory(dishCategory);
        dish.setFile(fileInfo);

        if (dto.getOptions() != null) {
            dish.getOptions().clear();
            dto.getOptions().forEach(option -> option.setDish(dish));
            dish.getOptions().addAll(dto.getOptions());
        }

        return dishRepository.save(dish);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public DishInfoDto updateDishForRestaurant(Long restaurantId, Long dishId, DishCreateDto dto) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new ResourceNotFoundException("Dish", dishId));

        if (!dish.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException("Dish", dishId);
        }

        if (dto.getTitle() != null) {
            dish.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            dish.setDescription(dto.getDescription());
        }
        if (dto.getPrice() != null) {
            dish.setPrice(dto.getPrice());
        }
        if (dto.getSort() != null) {
            dish.setSort(dto.getSort());
        }
        if (dto.getIsActive() != null) {
            dish.setIsActive(dto.getIsActive());
        }

        if (dto.getDishCategoryId() != null) {
            DishCategory category = dishCategoryRepository.findById(dto.getDishCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("DishCategory", dto.getDishCategoryId()));
            dish.setDishCategory(category);
        }

        Set<Option> updatedOptions = optionService.updateOptionsForDish(dish, dto.getOptions());
        Set<Option> existingOptions = dish.getOptions();

        existingOptions.clear();
        existingOptions.addAll(updatedOptions);

        Dish savedDish = dishRepository.save(dish);
        return dishMapper.dishUpdateInfoToDto(savedDish);
    }


    // ===== DELETE =====

    @Override
    public void deleteDish(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found", id));
        dishRepository.delete(dish);
    }

    // ===== PRIVATE =====

    private DishForClientDto mapToDishForClientDto(Dish dish) {
        Set<OptionForClientDto> optionDto = dish.getOptions().stream().map(option -> {
            Set<ElementForClientDto> elementDto = option.getElements().stream().map(element ->
                    new ElementForClientDto(
                            element.getId(),
                            element.getName(),
                            element.getDescription(),
                            element.getPrice(),
                            element.getIsActive(),
                            element.getIsDeleted()
                    )
            ).collect(Collectors.toSet());

            return new OptionForClientDto(
                    option.getId(),
                    option.getName(),
                    option.getRequired(),
                    option.getMin(),
                    option.getMax(),
                    option.getIsActive(),
                    elementDto
            );
        }).collect(Collectors.toSet());

        Long dishCategoryId = dish.getDishCategory() != null ? dish.getDishCategory().getId() : null;
        String filePath = dish.getFile() != null ? dish.getFile().getPath() : null;

        return new DishForClientDto(
                dish.getId(),
                dish.getTitle(),
                dish.getDescription(),
                dish.getIsActive(),
                dish.getIsDeleted(),
                dish.getPrice(),
                dish.getSort(),
                dish.getRestaurant().getId(),
                dishCategoryId,
                filePath,
                optionDto
        );
    }
}
