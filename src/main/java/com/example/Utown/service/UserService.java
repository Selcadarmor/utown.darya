package com.example.Utown.service;

import com.example.Utown.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    void changePassword(String username, String newPassword);
}

