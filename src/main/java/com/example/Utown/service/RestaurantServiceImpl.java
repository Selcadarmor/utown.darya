package com.example.Utown.service;

import com.example.Utown.dto.restaurantDTO.RestaurantCreateUpdateDto;
import com.example.Utown.dto.restaurantDTO.RestaurantDetailsDto;
import com.example.Utown.dto.restaurantDTO.RestaurantInfoDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.RestaurantMapper;
import com.example.Utown.model.Restaurant;
import com.example.Utown.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl  implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Override
    public List<RestaurantInfoDto> getAllRestaurants(){
        return restaurantRepository.findAllRestaurants().stream()
                .map(restaurantMapper::toRestaurantInfoDto)
                .toList();
    }

    @Override
    public RestaurantDetailsDto getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findRestaurantById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));
                return  restaurantMapper.toDto(restaurant);
    }

    @Transactional
    @Override
    public RestaurantDetailsDto createRestaurant(RestaurantCreateUpdateDto dto) {
        Restaurant restaurant = restaurantMapper.toEntity(dto);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(savedRestaurant);
    }

    @Transactional
    @Override
    public RestaurantDetailsDto updateRestaurant(Long id, RestaurantCreateUpdateDto dto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));
        restaurantMapper.updateFromDto(dto, restaurant);
        Restaurant restaurantUpdated = restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(restaurantUpdated);
    }
    @Transactional
    @Override
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));
        restaurantRepository.delete(restaurant);
    }
}
