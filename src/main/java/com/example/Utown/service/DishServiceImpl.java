package com.example.Utown.service;

import com.example.Utown.dto.dishDTO.DishDetailsDto;
import com.example.Utown.dto.dishDTO.DishDto;
import com.example.Utown.dto.dishDTO.DishForClientDto;
import com.example.Utown.dto.elementDTO.ElementForClientDto;
import com.example.Utown.dto.optionDTO.OptionForClientDto;
import com.example.Utown.exception.DishNotFoundException;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.DishMapper;
import com.example.Utown.model.Dish;
import com.example.Utown.model.DishCategory;
import com.example.Utown.model.FileInfo;
import com.example.Utown.model.Restaurant;
import com.example.Utown.repository.DishRepository;
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

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishCategoryRepository dishCategoryRepository;
    private final FileInfoRepository fileInfoRepository;
    private final DishMapper dishMapper;

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
    public DishDto getDishById(Long id) {
        return dishRepository.findDishById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found", id));
    }

    @Override
    public List<DishDto> getAllDishes() {
        return dishRepository.findAllDishes();
    }

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

    @Override
    public void deleteDish(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found", id));
        dishRepository.delete(dish);
    }

    @Override // метод получения вез Dish для каждого ресторана пагинация
    public Page<DishDetailsDto> getDishesByRestaurantId(Long restaurantId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("sort").ascending());
        return dishRepository.findByRestaurantId(restaurantId, pageable)
                .map(dishMapper::dishDetailsToDto);
    } //сделан не правилно не нужно ресторан айди сделать для конкретногореторана и ненужные поля поставить по умолчанию

    @Override // метод создания Dish для каждого ресторана, сделала так как не знаю менял ли кто-то метод создания Dish в будущем можно переиспользовать метод Dish createDish(DishDto dto)
    @Transactional(rollbackFor = Exception.class)
    public DishDetailsDto createDishForRestaurant(Long RestaurantId, DishDetailsDto dto) {
        Restaurant restaurant = restaurantRepository.findById(RestaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found", RestaurantId));


        DishCategory dishCategory = null;
        if (dto.getDishCategoryId() != null) {
            dishCategory = dishCategoryRepository.findById(dto.getDishCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("DishCategory not found", dto.getDishCategoryId()));
        }

        FileInfo file = null;
        if (dto.getFileId() != null) {
            file = fileInfoRepository.findById(dto.getFileId())
                    .orElseThrow(() -> new ResourceNotFoundException("File not found", dto.getFileId()));
        }
        Dish dish = Dish.builder()
                .description(dto.getDescription())
                .isActive(dto.getIsActive())
                .isDeleted(dto.getIsDeleted())
                .price(dto.getPrice())
                .title(dto.getTitle())
                .sort(dto.getSort())
                .dishCategory(dishCategory)
                .file(file)
                .restaurant(restaurant)
                .build();
        return dishMapper.toSavedDishDto(dishRepository.save(dish));// вот тут тоже посомтреть просто передовать айди в кнтроллео
    }

    @Override //  метод обновления Dish для каждого Restaurant хотела переиспользовать код из метода Dish updateDish(Long id, DishDto dto) но мне не ответили
    @Transactional(rollbackFor = Exception.class)
    public DishDetailsDto updateDishForRestaurant(Long RestaurantId, Long DishId, DishDetailsDto dto) {
        Dish dish = dishRepository.findById(DishId)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found", DishId));
        if(!dish.getRestaurant().getId().equals(RestaurantId)) {
            throw new ResourceNotFoundException("Dish not found", DishId);
        }

        DishDetailsDto dishDetailsDto = dishMapper.dishUpdateDetailsToDto(dish);
        dishDetailsDto.setTitle(dto.getTitle());
        dishDetailsDto.setDescription(dto.getDescription());
        dishDetailsDto.setPrice(dto.getPrice());
        dishDetailsDto.setSort(dto.getSort());
        dishDetailsDto.setDishCategoryId(dto.getDishCategoryId());
        dishDetailsDto.setFileId(dto.getFileId());
        dishDetailsDto.setRestaurantId(dto.getRestaurantId());
        dishDetailsDto.setIsActive(dto.getIsActive());
        dishDetailsDto.setIsDeleted(dto.getIsDeleted());
        return dishDetailsDto;
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

        return new DishForClientDto(
                dish.getId(),
                dish.getTitle(),
                dish.getDescription(),
                dish.getIsActive(),
                dish.getIsDeleted(),
                dish.getPrice(),
                dish.getSort(),
                dish.getRestaurant().getId(),
                dish.getDishCategory().getId(),
                dish.getFile() != null ? dish.getFile().getPath() : null,
                optionDto
        );
    }

}

