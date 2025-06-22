package com.example.Utown.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private Long defaultAddress;
    private String transport;
    private Long addressId;
    private boolean platform;
}
