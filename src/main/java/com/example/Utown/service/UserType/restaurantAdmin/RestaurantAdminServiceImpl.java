package com.example.Utown.service.UserType.restaurantAdmin;

import com.example.Utown.dto.adminDto.AdminDto;
import com.example.Utown.dto.restaurantAdminDto.RestaurantAdminCreateDto;
import com.example.Utown.dto.restaurantAdminDto.RestaurantAdminDto;
import com.example.Utown.exception.EntityNotFoundException;
import com.example.Utown.mapper.RestaurantAdminMapper;
import com.example.Utown.mapper.RoleMapper;
import com.example.Utown.model.Role;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.repository.RestaurantRepository;
import com.example.Utown.repository.RoleRepository;
import com.example.Utown.repository.UserType.RestaurantAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantAdminServiceImpl implements RestaurantAdminService {

    private final RestaurantAdminRepository restaurantAdminRepository;
    private final RestaurantAdminMapper restaurantAdminMapper;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantAdminMapper restaurantMapper;
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RestaurantAdminDto> getAllRestaurantAdmins() {
        return restaurantAdminRepository.findAll().stream()
                .map(restaurantAdminMapper::toDto)
                .toList();
    }

    @Override
    public RestaurantAdminDto getRestaurantAdminById(Long id) {
        RestaurantAdmin restaurantAdmin = restaurantAdminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return restaurantAdminMapper.toDto(restaurantAdmin);
    }

}
