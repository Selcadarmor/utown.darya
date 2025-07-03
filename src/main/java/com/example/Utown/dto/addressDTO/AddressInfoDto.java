package com.example.Utown.dto.addressDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressInfoDto {
    private Long id;
    private String area;
    private String city;
    private String details;
    private String fullAddress;
    private Float latitude;
    private Float longitude;
    private String postCode;
    private String state;
    private String street;
    private String intercomCode;
    private Integer typeAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}