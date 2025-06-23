package com.example.Utown.service;

import com.example.Utown.model.Role;
import com.example.Utown.model.enumFiles.Roles;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(Roles name);
}
