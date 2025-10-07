package com.example.Utown.dto.addressDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDto {
    private String requestId;
    private String street;
    private String city;
    private String details;
    private String area;
    private String fullAddress;
    private Float latitude;
    private Float longitude;
    private String postCode;
    private String state;
    private String intercomCode;
    private Integer typeAddress;
    private  Long clientId;
}