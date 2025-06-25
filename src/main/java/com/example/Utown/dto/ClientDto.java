package com.example.Utown.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {
    private Long id;
    private String username;
    private String city;
    private String fullAddress;
    private Long addressId;
    private Integer totalOrders;
    private Set<RoleDto> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
