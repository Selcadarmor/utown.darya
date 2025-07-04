package com.example.Utown.dto.clientDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientProfileUpdateDto {
    private String fullName;
    private String username;
    private String fullAddress;

    public ClientProfileUpdateDto(String fullName, String username, String fullAddress) {
        this.fullName = fullName;
        this.username = username;
        this.fullAddress = fullAddress;
    }
    public ClientProfileUpdateDto() {
    }
}




