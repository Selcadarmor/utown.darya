package com.example.Utown.service;

import com.example.Utown.model.Role;
import com.example.Utown.model.enumFiles.Roles;

public interface RoleService {
    Role findByName(Roles name);
}
