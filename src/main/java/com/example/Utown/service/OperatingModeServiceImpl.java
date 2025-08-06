package com.example.Utown.service;

import com.example.Utown.dto.operatingModeDTO.OperatingModeCreateDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeInfoDto;
import com.example.Utown.dto.operatingModeDTO.OperatingModeUpdateDto;
import com.example.Utown.exception.InvalidArgumentException;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.OperatingModeInfoMapper;
import com.example.Utown.model.OperatingMode;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.repository.OperatingModeRepository;
import com.example.Utown.repository.RestaurantRepository;
import com.example.Utown.service.UserTypeService.RestaurantAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OperatingModeServiceImpl implements OperatingModeService {

    private final OperatingModeRepository operatingModeRepository;
    private final OperatingModeInfoMapper operatingModeInfoMapper;
    private final RestaurantAdminService restaurantAdminService;
    private final RestaurantRepository restaurantRepository;

    // ===== GET =====

    @Override
    public OperatingModeInfoDto findById(Long id) {
        log.info("OperatingModeServiceImpl.findById: id={}", id);
        return operatingModeRepository.findProjectedById(id)
                .orElseThrow(() -> {
                    log.error("OperatingModeServiceImpl.findById: OperatingMode not found by id={}", id);
                    return new ResourceNotFoundException("OperatingMode", id);
                });
    }

    @Override
    public List<OperatingModeInfoDto> getOperatingModesByRestaurantId(Long restaurantId) {
        RestaurantAdmin currentAdmin = restaurantAdminService.getCurrentAdmin();
        log.info("OperatingModeServiceImpl.getOperatingModesByRestaurantId: restaurantId={}", restaurantId);
        if (!restaurantId.equals(currentAdmin.getRestaurant().getId())) {
            log.error("OperatingModeServiceImpl.getOperatingModesByRestaurantId: restaurantId={} does not match currentAdmin.restaurantId={}", restaurantId, currentAdmin.getRestaurant().getId());
            throw new AccessDeniedException("You do not have permission to access operating modes for this restaurant.");
        }
        List<OperatingMode> modes = operatingModeRepository.findByRestaurantId(restaurantId);
        log.info("Found {} operating modes for restaurantId={}", modes.size(), restaurantId);
        return operatingModeInfoMapper.toDtoList(modes);
    }

    // ===== POST =====

    @Override
    @Transactional
    public OperatingModeInfoDto createOperatingMode(OperatingModeCreateDto dto) {
        // Проверяем, существует ли режим с таким рестораном и днём недели
        boolean exists = operatingModeRepository.existsByRestaurantIdAndDayOfWeek(dto.getRestaurantId(), dto.getDayOfWeek());

        if (exists) {
            log.error("OperatingMode already exists for restaurantId={} and dayOfWeek={}", dto.getRestaurantId(), dto.getDayOfWeek());
            throw new InvalidArgumentException("dayOfWeek", dto.getDayOfWeek());
        }

        // Получаем ресторан
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> {
                    log.error("OperatingModeServiceImpl.createOperatingMode: Restaurant not found by id={}", dto.getRestaurantId());
                    return new ResourceNotFoundException("Restaurant", dto.getRestaurantId());
                });

        // Создаём новый OperatingMode
        OperatingMode mode = new OperatingMode();
        mode.setRestaurant(restaurant);
        mode.setDayOfWeek(dto.getDayOfWeek());
        mode.setStartTime(dto.getStartTime());
        mode.setEndTime(dto.getEndTime());
        mode.setDayOff(dto.isDayOff());
        log.debug("Created new OperatingMode: restaurantId={}, dayOfWeek={}, startTime={}, endTime={}",
                restaurant.getId(), dto.getDayOfWeek(), dto.getStartTime(), dto.getEndTime());

        // Сохраняем в базу
        OperatingMode saved = operatingModeRepository.save(mode);
        log.debug("Saved new OperatingMode: {}", saved);

        // Возвращаем DTO через JPQL-конструктор
        return operatingModeRepository.findProjectedById(saved.getId())
                .orElseThrow(() -> {
                    log.error("OperatingModeServiceImpl.createOperatingMode: Failed to retrieve OperatingMode by id={}", saved.getId());
                    return new ResourceNotFoundException("OperatingMode", saved.getId());
                });
    }



    // ===== PUT =====

    @Override
    @Transactional
    public OperatingModeInfoDto update(Long id, OperatingModeUpdateDto dto) {
        log.info("OperatingModeServiceImpl.update: id={}, dto={}", id, dto);
        OperatingMode updated = operatingModeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("OperatingModeServiceImpl.update: OperatingMode not found by id={}", id);
                    return new ResourceNotFoundException("OperatingMode", id);
                });

        RestaurantAdmin currentAdmin = restaurantAdminService.getCurrentAdmin();
        if (!updated.getRestaurant().getId().equals(currentAdmin.getRestaurant().getId())) {
            log.warn("OperatingModeServiceImpl.update: Access denied. AdminId={}, RestaurantId={}", currentAdmin.getId(), updated.getRestaurant().getId());
            throw new AccessDeniedException("You do not have permission to modify this operating mode.");
        }

        updated.setDayOff(dto.isDayOff());
        if (dto.getStartTime() != null) updated.setStartTime(dto.getStartTime());
        if (dto.getEndTime() != null) updated.setEndTime(dto.getEndTime());
        updated.setDayOfWeek(dto.getDayOfWeek());
        log.debug("Updated OperatingMode: {}", updated);

        operatingModeRepository.save(updated);
        return operatingModeRepository.findProjectedById(id)
                .orElseThrow(() -> {
                    log.error("OperatingModeServiceImpl.update: Failed to retrieve OperatingMode by id={}", id);
                    return new ResourceNotFoundException("OperatingMode", id);
                });
    }


    @Override
    @Transactional
    public void updateOperatingModes(List<OperatingModeUpdateDto> dtos) {
        log.info("OperatingModeServiceImpl.updateOperatingModes: received {} DTOs to update", dtos.size());

        for (OperatingModeUpdateDto dto : dtos) {
            if (dto.getId() != null) {
                update(dto.getId(), dto);
            } else {
                log.error("OperatingModeServiceImpl.updateOperatingModes: id is null in dto={}", dto);
                throw new InvalidArgumentException("operatingMode.id", null);
            }
        }
    }

}
