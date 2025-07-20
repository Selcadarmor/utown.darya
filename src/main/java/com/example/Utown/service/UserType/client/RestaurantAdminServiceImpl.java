package com.example.Utown.service.UserType.client;

import com.example.Utown.dto.restaurantAdminDTO.RestaurantAdminCreateDto;
import com.example.Utown.mapper.RestaurantAdminInfoMapper;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.Role;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.model.enumFiles.Roles;
import com.example.Utown.repository.RoleRepository;
import com.example.Utown.repository.UserType.RestaurantAdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@AllArgsConstructor
public class RestaurantAdminServiceImpl  implements RestaurantAdminService {
    private final RestaurantAdminRepository restaurantAdminRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestaurantAdminInfoMapper restaurantAdminInfoMapper;

    @Override
    @Transactional
    public RestaurantAdmin createAdmin(RestaurantAdminCreateDto dto, Restaurant restaurant) {
        RestaurantAdmin restaurantAdmin = restaurantAdminInfoMapper.toEntity(dto);
        restaurantAdmin.setPassword(passwordEncoder.encode(dto.getPassword()));
        Role role = roleRepository.findByName(Roles.ROLE_RESTAURANT_ADMIN)
                .orElseThrow(() -> new RuntimeException("Role not found: " + Roles.ROLE_RESTAURANT_ADMIN));
        restaurantAdmin.setRestaurant(restaurant);

        // Добавляем роль в пользователя, если roles еще не инициализирована
        if (restaurantAdmin.getRoles() == null) {
            restaurantAdmin.setRoles(new HashSet<>());
        }
        restaurantAdmin.getRoles().add(role);

        return restaurantAdminRepository.save(restaurantAdmin);
    }


}
