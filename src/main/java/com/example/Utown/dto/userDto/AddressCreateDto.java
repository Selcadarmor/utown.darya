package com.example.Utown.dto.userDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressCreateDto {

    @NotBlank
    private String fullAddress;
}


