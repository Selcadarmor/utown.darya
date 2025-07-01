package com.example.Utown.dto.userDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileUpdateDto {
    private String fullName;
    private String username;
    private String fullAddress;

    public UserProfileUpdateDto(String fullName, String username, String fullAddress) {
        this.fullName = fullName;
        this.username = username;
        this.fullAddress = fullAddress;
    }

    public UserProfileUpdateDto() {
    }
}




