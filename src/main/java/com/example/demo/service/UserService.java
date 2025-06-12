package com.example.demo.service;

import com.example.demo.dto.RegisterRequest;

public interface UserService {
    boolean existsByUsername(String username);
    void saveUser(RegisterRequest request);
}
