package com.example.Utown.service;

import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
import com.example.Utown.dto.restaurantDTO.RestaurantCreateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface RestaurantService {
    Page<RestaurantInfoDto> getAllRestaurants(int page, int size);
    RestaurantDetailsDto getRestaurantDetails(Long restaurantId);
    RestaurantDetailsDto createRestaurant(RestaurantCreateDto dto);
    RestaurantDetailsDto updateRestaurant(Long id, RestaurantUpdateDto dto);
    void deactivateRestaurant(Long restaurantId);
    List<RestaurantForClientDto> getRestaurantsAvailableForClient();
//    List<RestaurantForClientDto> getRestaurantsByCategoryId(Long categoryId);
//    List<RestaurantForClientDto> getAllRestaurantsSortedByDeliveryTime();
//    Page<RestaurantForClientDto> searchRestaurants(
//            String query,
//            int page,
//            int size,
//            String sortBy,
//            String direction);

}
