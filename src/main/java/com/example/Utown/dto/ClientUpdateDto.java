package com.example.Utown.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdateDto {
    private String username;
    private String fullName;
    private FileInfoDto fileInfoDto;
    private String fullAddress;
    private Set<RoleDto> roles;
}
