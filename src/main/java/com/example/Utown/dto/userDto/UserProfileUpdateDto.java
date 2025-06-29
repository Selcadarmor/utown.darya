package com.example.Utown.dto.userDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileUpdateDto {
    private String fullName;
    private String username;
    private AddressCreateDto defaultAddress;
}

