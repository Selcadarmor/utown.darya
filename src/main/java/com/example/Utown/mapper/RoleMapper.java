package com.example.Utown.mapper;

import com.example.Utown.dto.otherDto.RoleDto;
import com.example.Utown.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toEntity(RoleDto dto);
    RoleDto toDto(Role role);
}
