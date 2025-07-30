package com.example.Utown.service.UserTypeService;

import com.example.Utown.dto.AdminDTO.AdminRegistrationDto;
import com.example.Utown.exception.UserNotFoundException;
import com.example.Utown.model.Role;
import com.example.Utown.model.User;
import com.example.Utown.model.enumFiles.Roles;
import com.example.Utown.repository.UserRepository;
import com.example.Utown.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override //For Client
    public boolean changePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
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
        userRepository.save(user);
    }

}
