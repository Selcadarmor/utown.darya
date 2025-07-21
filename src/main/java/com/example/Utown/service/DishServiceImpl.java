package com.example.Utown.service;

import com.example.Utown.dto.dishDTO.DishCreateDto;
import com.example.Utown.dto.dishDTO.DishDetailsDto;
import com.example.Utown.dto.dishDTO.DishDto;
import com.example.Utown.dto.dishDTO.DishInfoDto;
import com.example.Utown.dto.elementDTO.ElementInfoDto;
import com.example.Utown.dto.optionDTO.OptionInfoDto;
import com.example.Utown.dto.dishDTO.DishForClientDto;
import com.example.Utown.dto.elementDTO.ElementForClientDto;
import com.example.Utown.dto.optionDTO.OptionForClientDto;
import com.example.Utown.exception.DishNotFoundException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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
    private final ElementRepository elementRepository;

    // ===== GET =====

    @Override
    public DishDto getDishById(Long id) {
        return dishRepository.findDishById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found", id));
    }

    @Override
    public Page<DishDetailsDto> getDishesByRestaurantId(Long restaurantId, int page, int size) {
        Page<Dish> dishPage = dishRepository.findByRestaurantId(restaurantId, PageRequest.of(page, size));

        List<Long> optionIds = dishPage.stream()
                .flatMap(dish -> dish.getOptions().stream())
                .map(Option::getId)
                .distinct()
                .toList();

        List<Option> optionsWithElements = optionRepository.findAllWithElementsByIds(optionIds);

        Map<Long, List<Element>> elementsMap = optionsWithElements.stream()
                .collect(Collectors.toMap(
                        Option::getId,
                        Option::getElements
                ));

        return dishPage.map(dish -> {
            List<OptionInfoDto> optionDtos = dish.getOptions().stream().map(option -> {
                List<ElementInfoDto> elementDtos = elementsMap.getOrDefault(option.getId(), List.of())
                        .stream()
                        .map(element -> new ElementInfoDto(
                                element.getId(),
                                element.getName(),
                                element.getPrice()
                        ))
                        .toList();

                return new OptionInfoDto(
                        option.getId(),
                        option.getName(),
                        elementDtos
                );
            }).toList();

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
    public DishForClientDto getDishByIdForClient(Long dishId) {
        Dish dish = dishRepository.findDishByIdForClient(dishId)
                .orElseThrow(() -> new DishNotFoundException(dishId));
        return mapToDishForClientDto(dish);
    }

    @Override
    public List<DishForClientDto> getDishesByCategoryForClient(Long categoryId) {
        List<Dish> dishes = dishRepository.findDishByCategoryForClient(categoryId);
        return dishes.stream()
                .map(this::mapToDishForClientDto)
                .toList();
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
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found", restaurantId));

        DishCategory dishCategory = null;
        if (dto.getDishCategoryId() != null) {
            dishCategory = dishCategoryRepository.findById(dto.getDishCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("DishCategory not found", dto.getDishCategoryId()));
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

        dishRepository.save(dish);

        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
            for (OptionInfoDto optionDto : dto.getOptions()) {
                Option option = Option.builder()
                        .name(optionDto.getName())
                        .dish(dish)
                        .build();
                optionRepository.save(option);

                if (optionDto.getElements() != null && !optionDto.getElements().isEmpty()) {
                    for (ElementInfoDto elementInfoDto : optionDto.getElements()) {
                        Element element = Element.builder()
                                .name(elementInfoDto.getName())
                                .price(elementInfoDto.getPrice())
                                .isActive(true)
                                .isDeleted(false)
                                .option(option)
                                .build();
                        elementRepository.save(element);
                    }
                }
            }
        }

        return dishMapper.toSavedDishDto(dish);
    }

    // ===== PUT =====

    @Override
    public Dish updateDish(Long id, DishDto dto) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish", id));

        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found", dto.getRestaurantId()));

        DishCategory dishCategory = dishCategoryRepository.findById(dto.getDishCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("DishCategory not found", dto.getDishCategoryId()));

        FileInfo fileInfo = fileInfoRepository.findById(dto.getFileId())
                .orElseThrow(() -> new ResourceNotFoundException("FileInfo not found", dto.getFileId()));

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

    @Transactional(rollbackFor = Exception.class)
    public DishInfoDto updateDishForRestaurant(Long restaurantId, Long dishId, DishCreateDto dto) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found", dishId));

        if (!dish.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException("Dish not found in this restaurant", dishId);
        }

        dish.setTitle(dto.getTitle());
        dish.setDescription(dto.getDescription());
        dish.setPrice(dto.getPrice());
        dish.setSort(dto.getSort());
        dish.setIsActive(dto.getIsActive());

        if (dto.getDishCategoryId() != null) {
            DishCategory category = dishCategoryRepository.findById(dto.getDishCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found", dto.getDishCategoryId()));
            dish.setDishCategory(category);
        }

        dish.getOptions().clear();

        if (dto.getOptions() != null) {
            for (OptionInfoDto optionDto : dto.getOptions()) {
                Option option = new Option();
                option.setName(optionDto.getName());
                option.setDish(dish);

                if (optionDto.getElements() != null) {
                    for (ElementInfoDto elementDto : optionDto.getElements()) {
                        Element element = new Element();
                        element.setName(elementDto.getName());
                        element.setPrice(elementDto.getPrice());
                        element.setIsActive(true);
                        element.setIsDeleted(false);
                        element.setOption(option);
                        option.getElements().add(element);
                    }
                }

                dish.getOptions().add(option);
            }
        }

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
        List<OptionForClientDto> optionDto = dish.getOptions().stream().map(option -> {
            List<ElementForClientDto> elementDto = option.getElements().stream().map(element ->
                    new ElementForClientDto(
                            element.getId(),
                            element.getName(),
                            element.getDescription(),
                            element.getPrice(),
                            element.getIsActive(),
                            element.getIsDeleted()
                    )
            ).toList();

            return new OptionForClientDto(
                    option.getId(),
                    option.getName(),
                    option.isRequired(),
                    option.getMin(),
                    option.getMax(),
                    option.getIsActive(),
                    elementDto
            );
        }).toList();

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
