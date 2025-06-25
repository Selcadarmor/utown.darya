package com.example.Utown.service.UserType.client;

import com.example.Utown.dto.userDto.UserChangePasswordDto;
import com.example.Utown.dto.userDto.UserProfileUpdateDto;
import com.example.Utown.dto.userDto.UserRegistrationDto;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.model.enumFiles.Roles;

import java.util.Optional;


public interface ClientService {
        Optional<Client> findByUsername(String username);
}


