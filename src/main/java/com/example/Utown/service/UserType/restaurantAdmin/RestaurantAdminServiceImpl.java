package com.example.Utown.service.UserType.restaurantAdmin;

import com.example.Utown.dto.restaurantAdmin.RestaurantAdminCreateDto;
import com.example.Utown.dto.restaurantAdmin.RestaurantAdminDto;
import com.example.Utown.dto.restaurantAdmin.RestaurantAdminUpdateDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.RestaurantAdminMapper;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.repository.RestaurantRepository;
import com.example.Utown.repository.UserType.RestaurantAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantAdminServiceImpl implements RestaurantAdminService {

    private final RestaurantAdminRepository restaurantAdminRepository;
    private final RestaurantAdminMapper restaurantAdminMapper;
    private final RestaurantRepository restaurantRepository;


    @Override
    public List<RestaurantAdminDto> getAllRestaurantAdmins() {
        return restaurantAdminRepository.findAll().stream()
                .map(restaurantAdminMapper::toDto)
                .toList();
    }

    @Override
    public RestaurantAdminDto getRestaurantAdminById(Long id) {
        RestaurantAdmin restaurantAdmin = restaurantAdminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RestaurantAdmin", id));
        return restaurantAdminMapper.toDto(restaurantAdmin);
    }
    @Transactional
    @Override
    public RestaurantAdminDto createAdmin(RestaurantAdminCreateDto createDto) {
        RestaurantAdmin restaurantAdmin = restaurantAdminMapper.toRestaurantAdmin(createDto);

        if (createDto.getRestaurantId() != null) {
            Restaurant restaurant = restaurantRepository.findById(createDto.getRestaurantId())
                    .orElseThrow(() -> new ResourceNotFoundException("RestaurantAdmin", createDto.getRestaurantId()));

            if (restaurant.getRestaurantAdmin() != null) {
                throw new ResourceNotFoundException("RestaurantAdmin", restaurant.getId());
            }

            restaurantAdmin.setRestaurant(restaurant);
            restaurant.setRestaurantAdmin(restaurantAdmin);
        }

        RestaurantAdmin savedRestaurantAdmin = restaurantAdminRepository.save(restaurantAdmin);
        return restaurantAdminMapper.toDto(savedRestaurantAdmin);
    }


    @Transactional
    @Override
    public RestaurantAdminDto updateRestaurantAdmin(Long id, RestaurantAdminUpdateDto dto) {
        RestaurantAdmin restaurantAdmin = restaurantAdminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RestaurantAdmin", id));
        restaurantAdminMapper.updateRestaurantAdminFromDto(dto, restaurantAdmin);
        return restaurantAdminMapper.toDto(restaurantAdminRepository.save(restaurantAdmin));
    }

    @Transactional
    @Override
    public void deleteRestaurantAdmin(Long id) {
        RestaurantAdmin restaurantAdmin = restaurantAdminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RestaurantAdmin", id));
        restaurantAdminRepository.delete(restaurantAdmin);
    }
}
