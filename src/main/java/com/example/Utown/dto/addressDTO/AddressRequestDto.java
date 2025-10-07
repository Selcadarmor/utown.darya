package com.example.Utown.dto.addressDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressRequestDto {
    private String requestId;
    private String street;
    private String city;
    private String details;
    private String intercomCode;
}