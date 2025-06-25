package com.example.Utown.service;

import com.example.Utown.dto.userDto.UserChangePasswordDto;
import com.example.Utown.dto.userDto.UserProfileUpdateDto;
import com.example.Utown.dto.userDto.UserRegistrationDto;
import com.example.Utown.model.User;
import com.example.Utown.model.enumFiles.Roles;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    void save(UserRegistrationDto dto, Roles roleName);
    void updateProfile(String currentUsername, UserProfileUpdateDto dto);
    void changePassword(String username, UserChangePasswordDto dto);
}

