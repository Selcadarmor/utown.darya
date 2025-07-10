package com.example.Utown.service;

import com.example.Utown.dto.userDto.UserChangePasswordDto;
import com.example.Utown.dto.clientDTO.ClientRegistrationDto;
import com.example.Utown.model.User;
import com.example.Utown.model.enumFiles.Roles;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    void changePassword(String username, UserChangePasswordDto dto);
}

