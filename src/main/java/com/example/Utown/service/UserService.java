package com.example.Utown.service;

import com.example.Utown.dto.JWTRequest;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    void saveUser(JWTRequest request);
}
