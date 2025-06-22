package com.example.Utown.dto.adminDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateRequestDto {
    @NotBlank
    private String username;

    @NotBlank
    private Long defaultAddress;

    @NotBlank
    private  Long addressId;

    @NotBlank
    private String transport;

    @NotBlank
    private String fullName;

    @NotBlank
    private Boolean isActive;
}
