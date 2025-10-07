package com.example.Utown.mapper;

import com.example.Utown.model.Role;
import com.example.Utown.model.enumFiles.Roles;
import com.example.Utown.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

    private final RoleRepository roleRepository;

    public RoleMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role map(Roles roleEnum) {
        return roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleEnum));
    }

    public Set<Role> mapRoles(Set<Roles> roleEnums) {
        return roleEnums.stream()
                .map(this::map)
                .collect(Collectors.toSet());
    }
}
