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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OperatingModeServiceImpl implements OperatingModeService {

    private final OperatingModeRepository operatingModeRepository;
    private final OperatingModeInfoMapper operatingModeInfoMapper;
    private final RestaurantAdminService restaurantAdminService;
    private final RestaurantRepository restaurantRepository;

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
        RestaurantAdmin currentAdmin = restaurantAdminService.getCurrentAdmin();
        if (!restaurantId.equals(currentAdmin.getRestaurant().getId())) {
            throw new AccessDeniedException("You do not have permission to access operating modes for this restaurant.");
        }
        List<OperatingMode> modes = operatingModeRepository.findByRestaurantId(restaurantId);
        return operatingModeInfoMapper.toDtoList(modes);
    }

    // ===== POST =====

    @Override
    @Transactional
    public OperatingModeInfoDto createOperatingMode(OperatingModeCreateDto dto) {
        // Проверяем, существует ли режим с таким рестораном и днём недели
        boolean exists = operatingModeRepository.existsByRestaurantIdAndDayOfWeek(dto.getRestaurantId(), dto.getDayOfWeek());

        if (exists) {
            throw new IllegalStateException("Operating mode for this restaurant and dayOfWeek already exists");
        }

        // Получаем ресторан
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", dto.getRestaurantId()));

        // Создаём новый OperatingMode
        OperatingMode mode = new OperatingMode();
        mode.setRestaurant(restaurant);
        mode.setDayOfWeek(dto.getDayOfWeek());
        mode.setStartTime(dto.getStartTime());
        mode.setEndTime(dto.getEndTime());
        mode.setDayOff(dto.isDayOff());

        // Сохраняем в базу
        OperatingMode saved = operatingModeRepository.save(mode);

        // Возвращаем DTO через JPQL-конструктор
        return operatingModeRepository.findProjectedById(saved.getId())
                .orElseThrow(() -> new ResourceNotFoundException("OperatingMode", saved.getId()));
    }



    // ===== PUT =====

    @Override
    @Transactional
    public OperatingModeInfoDto update(Long id, OperatingModeUpdateDto dto) {
        OperatingMode updated = operatingModeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OperatingMode", id));

        RestaurantAdmin currentAdmin = restaurantAdminService.getCurrentAdmin();
        if (!updated.getRestaurant().getId().equals(currentAdmin.getRestaurant().getId())) {
            throw new AccessDeniedException("You do not have permission to modify this operating mode.");
        }

        updated.setDayOff(dto.isDayOff());
        if (dto.getStartTime() != null) updated.setStartTime(dto.getStartTime());
        if (dto.getEndTime() != null) updated.setEndTime(dto.getEndTime());
        updated.setDayOfWeek(dto.getDayOfWeek());

        operatingModeRepository.save(updated);
        return operatingModeRepository.findProjectedById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OperatingMode", id));
    }


    @Override
    @Transactional
    public void updateOperatingModes(List<OperatingModeUpdateDto> dtos) {
        for (OperatingModeUpdateDto dto : dtos) {
            if (dto.getId() != null) {
                update(dto.getId(), dto);
            } else {
                throw new InvalidArgumentException("operatingMode.id", null);
            }
        }
    }

}
