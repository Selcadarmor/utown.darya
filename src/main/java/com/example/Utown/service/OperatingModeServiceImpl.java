package com.example.Utown.service;

import com.example.Utown.dto.operatingModeDTO.OperatingModeCreateDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeUpdateDto;
import com.example.Utown.exception.ElementNotFoundException;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.model.OperatingMode;
import com.example.Utown.model.Restaurant;
import com.example.Utown.repository.OperatingModeRepository;
import com.example.Utown.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OperatingModeServiceImpl implements OperatingModeService {

    private final OperatingModeRepository operatingModeRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public List<OperatingModeInfoDto> findAll() {
        return operatingModeRepository.findAllOperatingModes();
    }

    @Override
    public OperatingModeInfoDto findById(Long id) {
        return operatingModeRepository.findProjectedById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OperatingMode", id));
    }

    @Override
    @Transactional
    public  OperatingModeInfoDto create(OperatingModeCreateDto dto) {
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found", dto.getRestaurantId()));

       OperatingMode operatingMode = new OperatingMode();
       operatingMode.setDayOff(dto.isDayOff());
       operatingMode.setRestaurant(restaurant);
       operatingMode.setDayOfWeek(dto.getDayOfWeek());
       operatingMode.setEnd(dto.getEnd());
       operatingMode.setStart(dto.getStart());
        OperatingMode saved = operatingModeRepository.save(operatingMode);
       return operatingModeRepository.findProjectedById(saved.getId())
               .orElseThrow(() -> new ElementNotFoundException("OperatingMode"));
    }

    @Override
    @Transactional
    public OperatingModeInfoDto update(Long id, OperatingModeUpdateDto dto) {
        OperatingMode updated = operatingModeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OperatingMode", id));

        updated.setDayOff(dto.isDayOff());
        updated.setStart(LocalDateTime.parse(dto.getStart()));
        updated.setEnd(LocalDateTime.parse(dto.getEnd()));
        updated.setDayOfWeek(dto.getDayOfWeek());
        updated.setUpdatedAt(LocalDateTime.now());

        return operatingModeRepository.findProjectedById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OperatingMode", id));
    }


    @Override
    public void deleteAllByRestaurantId(Long restaurantId) {
        operatingModeRepository.deleteById(restaurantId);
    }

    @Override
    public List<OperatingModeInfoDto> createAll(List<OperatingModeCreateDto> dtos) {
        return dtos.stream()
                .map(this::create)
                .collect(Collectors.toList());
    }

}
