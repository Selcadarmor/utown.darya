package com.example.Utown.service;

import com.example.Utown.model.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(String name);
}
