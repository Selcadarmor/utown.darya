package com.example.Utown.service.UserType.restaurant;

import com.example.Utown.dto.adminDto.OperatingModeDto;
import com.example.Utown.dto.adminDto.RestaurantCreateUpdateDto;
import com.example.Utown.dto.adminDto.RestaurantDetailsDto;
import com.example.Utown.dto.adminDto.RestaurantInfoDto;
import com.example.Utown.dto.clientDto.RestaurantDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.model.Address;
import com.example.Utown.model.OperatingMode;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl  implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final OperatingModeRepository operatingModeRepository;
    private final FileInfoRepository fileInfoRepository;


    @Override
    public List<RestaurantInfoDto> getAllRestaurants() {
        return restaurantRepository.findAllRestaurantInfos();
    }

    @Override
    public RestaurantDetailsDto getRestaurantById(Long id) {
        RestaurantDetailsDto dto = restaurantRepository.findRestaurantDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));
        List<OperatingMode> entities = operatingModeRepository.findByRestaurantId(id);
        List<OperatingModeDto> dtos = entities.stream()
                    .map(m -> new OperatingModeDto(
                        m.getId(),
                        m.getStart(),
                        m.getEnd(),
                        m.getDayOff()
                ))
                .collect(Collectors.toList());
        dto.setOperatingModes(dtos);
        return dto;
    }

    @Transactional
    @Override
    public RestaurantDetailsDto createRestaurant(RestaurantCreateUpdateDto dto) {
        Address address = addressRepository.
        Restaurant restaurant = restauran.toEntity(dto);
        if (dto.getAddress() != null) {
            Address address = addressRepository.findById(dto.getAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("Address", dto.getAddressId()));
            restaurant.setAddress(address);
        }

        if (dto.getUserId() != null) {
            RestaurantAdmin admin = restaurantAdminRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("RestaurantAdmin", dto.getRestaurantAdminId()));
            restaurant.setRestaurantAdmin(admin);
            admin.setRestaurant(restaurant);
        }
        return restaurantMapper.toDto(restaurantRepository.save(restaurant));
    }

    @Transactional
    @Override
    public RestaurantDto updateRestaurant(Long id, RestaurantDto dto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));

        restaurantMapper.updateRestaurantFromDto(dto, restaurant);

        if (dto.getAddressId() != null) {
            Address address = addressRepository.findById(dto.getAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException( "Address", dto.getAddressId()));
            restaurant.setAddress(address);
        }
        if (dto.getRestaurantAdminId() != null) {
            RestaurantAdmin admin = restaurantAdminRepository.findById(dto.getRestaurantAdminId())
                    .orElseThrow(() -> new ResourceNotFoundException("RestaurantAdmin", dto.getRestaurantAdminId()));
            restaurant.setRestaurantAdmin(admin);
            admin.setRestaurant(restaurant);
        }
        return restaurantMapper.toDto(restaurantRepository.save(restaurant));
    }

    @Transactional
    @Override
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));
        restaurantRepository.delete(restaurant);
    }
}
