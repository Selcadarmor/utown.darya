package com.example.Utown.service.UserType.restaurant;

import com.example.Utown.dto.RestaurantDto;
import com.example.Utown.exception.EntityNotFoundException;
import com.example.Utown.mapper.AddressMapper;
import com.example.Utown.mapper.RestaurantMapper;
import com.example.Utown.model.Address;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.repository.AddressRepository;
import com.example.Utown.repository.RestaurantRepository;
import com.example.Utown.repository.UserType.RestaurantAdminRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
@RequiredArgsConstructor
@Builder
public class RestaurantServiceImpl  implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final AddressRepository addressRepository;
    private final AddressMapper  addressMapper;
    private final RestaurantAdminRepository restaurantAdminRepository;


    @Override
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    @Override
    public RestaurantDto getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return restaurantMapper.toDto(restaurant);
    }

    @Transactional
    @Override
    public RestaurantDto createRestaurant(RestaurantDto dto) {
        Restaurant restaurant = restaurantMapper.toEntity(dto);
        if (dto.getAddress() != null) {
            Address address = addressRepository.findById(dto.getAddressId())
                    .orElseThrow(() -> new EntityNotFoundException(dto.getAddressId()));
            restaurant.setAddress(address);
        }

        if (dto.getRestaurantAdminId() != null) {
            RestaurantAdmin admin = restaurantAdminRepository.findById(dto.getRestaurantAdminId())
                    .orElseThrow(() -> new EntityNotFoundException(dto.getRestaurantAdminId()));
            restaurant.setRestaurantAdmin(admin);
            admin.setRestaurant(restaurant);
        }
        return restaurantMapper.toDto(restaurantRepository.save(restaurant));
    }

    @Transactional
    @Override
    public RestaurantDto updateRestaurant(Long id, RestaurantDto dto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        restaurantMapper.updateFromDto(dto, restaurant);

        if (dto.getAddress() != null) {
            Address address = addressRepository.findById(dto.getAddressId())
                    .orElseThrow(() -> new EntityNotFoundException(dto.getAddressId()));
            restaurant.setAddress(address);
        }
        if (dto.getRestaurantAdminId() != null) {
            RestaurantAdmin admin = restaurantAdminRepository.findById(dto.getRestaurantAdminId())
                    .orElseThrow(() -> new EntityNotFoundException(dto.getRestaurantAdminId()));
            restaurant.setRestaurantAdmin(admin);
            admin.setRestaurant(restaurant);
        }
        return restaurantMapper.toDto(restaurantRepository.save(restaurant));
    }

    @Transactional
    @Override
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        restaurantRepository.delete(restaurant);
    }
}
