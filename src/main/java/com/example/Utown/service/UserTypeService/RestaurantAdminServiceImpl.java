package com.example.Utown.service.UserTypeService;

import com.example.Utown.dto.restaurantAdminDTO.RestaurantAdminCreateDto;
import com.example.Utown.exception.RoleNotFoundException;
import com.example.Utown.exception.UserNotFoundException;
import com.example.Utown.mapper.RestaurantAdminInfoMapper;
import com.example.Utown.model.Restaurant;
import com.example.Utown.model.Role;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.model.enumFiles.Roles;
import com.example.Utown.repository.RoleRepository;
import com.example.Utown.repository.UserType.RestaurantAdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Transactional
    @Override
    public RestaurantAdmin createAdmin(RestaurantAdminCreateDto dto, Restaurant restaurant) {
        if (dto == null) {
            throw new IllegalArgumentException("RestaurantAdminCreateDto must not be null");
        }

        RestaurantAdmin admin = restaurantAdminInfoMapper.toEntity(dto);

        // Защита от null в password
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            System.out.println("Received password: " + dto.getPassword());
            admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        } else {
            throw new IllegalArgumentException("Password must not be null or blank");
        }

        Role role = roleRepository.findByName(Roles.ROLE_RESTAURANT_ADMIN)
                .orElseThrow(() -> new RoleNotFoundException(Roles.ROLE_RESTAURANT_ADMIN.name()));

        admin.setRestaurant(restaurant);

        if (admin.getRoles() == null) {
            admin.setRoles(new HashSet<>());
        }
        admin.getRoles().add(role);

        return restaurantAdminRepository.save(admin);
    }

    @Override
    public RestaurantAdmin getCurrentAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        String username = authentication.getName();
        return restaurantAdminRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Admin not found: " + username));
    }

}
