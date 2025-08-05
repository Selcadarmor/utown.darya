package com.example.Utown.service;

import com.example.Utown.dto.restaurantDTO.RestaurantCreateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;
import com.example.Utown.dto.restaurantDTO.RestaurantProfileDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateResponseDto;
import com.example.Utown.dto.restaurantDTO.RestaurantsCreateResponseDto;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.enumFiles.RestaurantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantService {

    Restaurant findRestaurantById(Long restaurantId);
    Page<RestaurantInfoDto> getAllRestaurants(String query, Boolean isActive, int page, int size);
    RestaurantDetailsDto getRestaurantDetails(Long restaurantId);
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
    RestaurantsCreateResponseDto createRestaurant(RestaurantCreateDto dto);
    RestaurantUpdateResponseDto updateRestaurant(Long id, RestaurantUpdateDto dto);
    RestaurantUpdateResponseDto updateRestaurantByAdmin(RestaurantUpdateDto dto);
    void updateStatusForCurrentAdminRestaurant(RestaurantStatus newStatus);
    void deactivateRestaurant(Long restaurantId);

}
