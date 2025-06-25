package com.example.Utown.service;

import com.example.Utown.dto.userDto.UserChangePasswordDto;
import com.example.Utown.dto.userDto.UserProfileUpdateDto;
import com.example.Utown.dto.userDto.UserRegistrationDto;
import com.example.Utown.exception.PasswordsDoNotMatchException;
import com.example.Utown.exception.RoleNotFoundException;
import com.example.Utown.exception.UserAlreadyExistsException;
import com.example.Utown.exception.UserNotFoundException;
import com.example.Utown.model.Role;
import com.example.Utown.model.User;
import com.example.Utown.model.enumFiles.Roles;
import com.example.Utown.repository.RoleRepository;
import com.example.Utown.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void save(UserRegistrationDto dto, Roles roleName) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(dto.getUsername());
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName.name()));

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Set.of(role));
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void updateProfile(String currentUsername, UserProfileUpdateDto dto) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserNotFoundException(currentUsername));

        if (!user.getUsername().equals(dto.getUsername())
                && userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(dto.getUsername());
        }

        user.setFullName(dto.getFullName());
        user.setUsername(dto.getUsername());
        user.setDefaultAddress(dto.getDefaultAddress());

        userRepository.save(user);
    }

    @Override
    public void changePassword(String username, UserChangePasswordDto dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}
