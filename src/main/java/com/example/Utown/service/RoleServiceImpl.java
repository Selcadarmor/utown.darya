package com.example.Utown.service;

import com.example.Utown.exception.RoleNotFoundException;
import com.example.Utown.model.Role;
import com.example.Utown.model.enumFiles.Roles;
import com.example.Utown.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByName(Roles name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException(name.name()));
    }
}

