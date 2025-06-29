package com.example.Utown.service.UserType.restaurant;

import com.example.Utown.dto.adminDto.OperatingModeDto;
import com.example.Utown.dto.adminDto.RestaurantCreateUpdateDto;
import com.example.Utown.dto.adminDto.RestaurantDetailsDto;
import com.example.Utown.dto.adminDto.RestaurantInfoDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.OperatingModeMapper;
import com.example.Utown.model.*;
import com.example.Utown.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OperatingModeMapper operatingModeMapper;


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
                        m.getDayOff(),
                        m.getDayOfWeek()
                ))
                .collect(Collectors.toList());
        dto.setOperatingModes(dtos);
        return dto;
    }

    @Transactional
    @Override
    public RestaurantDetailsDto createRestaurant(RestaurantCreateUpdateDto dto) {
        Address address = addressRepository.findByCity(dto.getCity())
                .orElseThrow(() -> new ResourceNotFoundException("Address (city)", dto.getCity()));

        RestaurantCategory restaurantCategory = restaurantCategoryRepository.findByName(dto.getName())
                .orElseThrow(() -> new ResourceNotFoundException("RestaurantCategory (title)", dto.getName()));

        FileInfo fileInfo = null;
        if (dto.getFileInfoId() != null) {
            fileInfo = fileInfoRepository.findById(dto.getFileInfoId())
                    .orElseThrow(() -> new ResourceNotFoundException("FileInfo", dto.getFileInfoId()));
        }

        Restaurant restaurant = Restaurant.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .phone(dto.getPhone())
                .minOrderAmount(dto.getMinOrderAmount())
                .fileInfo(fileInfo)
                .address(address)
                .category(restaurantCategory)
                .build();

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        if (dto.getOperatingModes() != null && !dto.getOperatingModes().isEmpty()) {
            List<OperatingMode> modes = dto.getOperatingModes().stream()
                    .map(modeDto -> {
                        OperatingMode mode = new OperatingMode();
                        mode.setStart(modeDto.getStart());
                        mode.setEnd(modeDto.getEnd());
                        mode.setDayOff(modeDto.isDayOff());
                        mode.setDayOfWeek(modeDto.getStart().getDayOfWeek().getValue());
                        mode.setRestaurant(savedRestaurant);
                        return mode;
                    })
                    .collect(Collectors.toList());
            operatingModeRepository.saveAll(modes);
        }

        return getRestaurantById(restaurant.getId());
    }


    @Transactional
    @Override
    public RestaurantDetailsDto updateRestaurant(Long id, RestaurantCreateUpdateDto dto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));
        Address address = addressRepository.findByCity(dto.getCity())
                .orElseThrow(() -> new ResourceNotFoundException("Address (city)", dto.getCity()));

        RestaurantCategory restaurantCategory = restaurantCategoryRepository.findByName(dto.getName())
                .orElseThrow(() -> new ResourceNotFoundException("RestaurantCategory (title)", dto.getTitle()));
        FileInfo fileInfo = null;
        if (dto.getFileInfoId() != null) {
            fileInfo = fileInfoRepository.findById(dto.getFileInfoId())
                    .orElseThrow(() -> new ResourceNotFoundException("FileInfo", dto.getFileInfoId()));
        }
        restaurant.setFileInfo(fileInfo);
        restaurant.setTitle(dto.getTitle());
        restaurant.setDescription(dto.getDescription());
        restaurant.setPhone(dto.getPhone());
        restaurant.setMinOrderAmount(dto.getMinOrderAmount());
        restaurant.setAddress(address);
        restaurant.setCategory(restaurantCategory);
        restaurantRepository.save(restaurant);

        List<OperatingMode> operatingModes = operatingModeRepository.findByRestaurantId(restaurant.getId());
        List<OperatingModeDto> operatingModeDtos = operatingModes.stream()
                .map(operatingModeMapper::toDto)
                .collect(Collectors.toList());


        RestaurantDetailsDto restaurantDetailsDto = getRestaurantById(restaurant.getId());
        restaurantDetailsDto.setOperatingModes(operatingModeDtos);
        return restaurantDetailsDto;
    }

    @Transactional
    @Override
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));
        restaurantRepository.delete(restaurant);
    }
}



