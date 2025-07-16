package com.example.Utown.service;

import com.example.Utown.dto.restaurantDTO.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantService {
    Page<RestaurantInfoDto> getAllRestaurants(int page, int size);
    RestaurantDetailsDto getRestaurantDetails(Long restaurantId);
    RestaurantDetailsDto createRestaurant(RestaurantCreateDto dto);
    RestaurantDetailsDto updateRestaurant(Long id, RestaurantUpdateDto dto);
    Page<RestaurantForClientDto> getRecommendedRestaurantsForClient(Pageable pageable);
    Page<RestaurantForClientDto> getFastestDeliveryRestaurantsForClient(Pageable pageable);
    Page<RestaurantForClientDto> getRestaurantsByCategory(Long categoryId, Pageable pageable);
    Page<RestaurantForClientDto> searchRestaurants(
            String query,
            int page,
            int size,
            String sortBy,
            String direction);
    RestaurantProfileDto getRestaurantProfile(Long restaurantId);
    void deactivateRestaurant(Long restaurantId);

}
