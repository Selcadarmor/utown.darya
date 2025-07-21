package com.example.Utown.service;

import com.example.Utown.dto.operatingModeDTO.OperatingModeCreateDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeUpdateDto;
import com.example.Utown.exception.ElementNotFoundException;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.OperatingModeInfoMapper;
import com.example.Utown.model.OperatingMode;
import com.example.Utown.model.Restaurant;
import com.example.Utown.repository.OperatingModeRepository;
import com.example.Utown.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class OperatingModeServiceImpl implements OperatingModeService {

    private final OperatingModeRepository operatingModeRepository;
    private final RestaurantRepository restaurantRepository;
    private final OperatingModeInfoMapper operatingModeInfoMapper;

    // ===== GET =====

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
    public List<OperatingModeInfoDto> getOperatingModesByRestaurantId(Long restaurantId) {
        List<OperatingMode> modes = operatingModeRepository.findByRestaurantId(restaurantId);
        return operatingModeInfoMapper.toDtoList(modes);
    }

    // ===== POST =====

    @Override
    @Transactional
    public OperatingModeInfoDto create(OperatingModeCreateDto dto) {
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found", dto.getRestaurantId()));

        Optional<OperatingMode> existingMode = operatingModeRepository
                .findByRestaurantIdAndDayOfWeek(dto.getRestaurantId(), dto.getDayOfWeek());

        OperatingMode operatingMode;
        if (existingMode.isPresent()) {
            // обновляем существующий режим
            operatingMode = existingMode.get();
        } else {
            // создаём новый
            operatingMode = new OperatingMode();
            operatingMode.setRestaurant(restaurant);
        }

        operatingMode.setDayOff(dto.isDayOff());
        operatingMode.setDayOfWeek(dto.getDayOfWeek());
        operatingMode.setStartTime(dto.getStartTime());
        operatingMode.setEndTime(dto.getEndTime());

        OperatingMode saved = operatingModeRepository.save(operatingMode);

        return operatingModeRepository.findProjectedById(saved.getId())
                .orElseThrow(() -> new ElementNotFoundException("OperatingMode"));
    }

    @Override
    public List<OperatingModeInfoDto> createAll(List<OperatingModeCreateDto> dtos) {
        return dtos.stream()
                .map(this::create)
                .collect(Collectors.toList());
    }

    // ===== PUT =====

    @Override
    @Transactional
    public OperatingModeInfoDto update(Long id, OperatingModeUpdateDto dto) {
        OperatingMode updated = operatingModeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OperatingMode", id));

        updated.setDayOff(dto.isDayOff());
        if (dto.getStartTime() != null) updated.setStartTime(dto.getStartTime());
        if (dto.getEndTime() != null) updated.setEndTime(dto.getEndTime());
        updated.setDayOfWeek(dto.getDayOfWeek());

        return operatingModeRepository.findProjectedById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OperatingMode", id));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateOperatingModes(Restaurant restaurant, List<OperatingModeUpdateDto> dtos) {
        List<OperatingMode> newModes = dtos.stream()
                .map(dto -> {
                    OperatingMode mode = new OperatingMode();
                    mode.setRestaurant(restaurant);
                    mode.setDayOfWeek(dto.getDayOfWeek());
                    mode.setStartTime(dto.getStartTime());
                    mode.setEndTime(dto.getEndTime());
                    return mode;
                })
                .collect(Collectors.toList());

        operatingModeRepository.saveAll(newModes);
    }

    // ===== DELETE =====
    // Если понадобится метод удаления, например:
    // @Override
    // public void deleteById(Long id) {
    //    operatingModeRepository.deleteById(id);
    // }
}
