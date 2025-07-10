package com.example.Utown.service;

import com.example.Utown.dto.userDto.UserChangePasswordDto;
import com.example.Utown.dto.clientDTO.ClientRegistrationDto;
import com.example.Utown.exception.*;
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

    @Override //For Client
    public void changePassword(String username, UserChangePasswordDto dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

}
