package com.example.Utown.service.UserTypeService;

import com.example.Utown.dto.AdminDTO.AdminRegistrationDto;
import com.example.Utown.exception.UserNotFoundException;
import com.example.Utown.model.Role;
import com.example.Utown.model.UserType.User;
import com.example.Utown.model.enumFiles.Roles;
import com.example.Utown.repository.UserRepository;
import com.example.Utown.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public Optional<User> findByUsername(String username) {
        log.debug("Searching for user by username: {}", username);
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            log.info("User found with username: {}", username);
        } else {
            log.warn("No user found with username: {}", username);
        }

        return user;
    }

    @Override //For Client
    public boolean changePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password changed successfully for user: {}", username);
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void createAdmin(AdminRegistrationDto adminRegistrationDto, Roles roles) {
        Role role = roleService.findByName(roles);

        User user = new User();
        user.setUsername(adminRegistrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(adminRegistrationDto.getPassword()));
        user.setRoles(Set.of(role));
        user.setNotifications(new HashSet<>());
        user.setIsActive(true);
        userRepository.save(user);
        log.info("Admin user created successfully: {}", adminRegistrationDto.getUsername());
    }

}
