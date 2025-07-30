package com.example.Utown.service.UserTypeService;

import com.example.Utown.dto.AdminDTO.AdminRegistrationDto;
import com.example.Utown.model.User;
import com.example.Utown.model.enumFiles.Roles;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    boolean changePassword(String username, String newPassword);
    void createAdmin(AdminRegistrationDto adminRegistrationDto, Roles roles);
}

