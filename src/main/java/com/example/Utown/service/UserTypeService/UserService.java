package com.example.Utown.service.UserTypeService;

import com.example.Utown.model.UserType.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    boolean changePassword(String username, String newPassword);
}

