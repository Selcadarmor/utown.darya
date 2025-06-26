package com.example.Utown.service;

import com.example.Utown.dto.clientDto.RestaurantCategoryDto;
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

    @Override
    public List<RestaurantCategoryDto> getAllCategoriesWithCount() {
        List<RestaurantCategory> categories = restaurantCategoryRepository.findAll();

        return categories.stream()
                .map(category -> {
                    Long count = restaurantRepository.countByCategory(category);
                    return new RestaurantCategoryDto(
                            category.getId(),
                            category.getName(),
                            category.getImageUrl(),
                            count
                    );
                })
                .toList();
    }
}

