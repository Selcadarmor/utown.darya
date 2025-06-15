package com.example.Utown.service;

import com.example.Utown.dto.JWTRequest;
import com.example.Utown.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    public void saveUser(JWTRequest request, String roleName);
}

