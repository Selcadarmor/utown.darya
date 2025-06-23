package com.example.Utown.dto.clientDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientProfileUpdateDto {
    private String fullName;
    private String username;
    private Long defaultAddress;
}

