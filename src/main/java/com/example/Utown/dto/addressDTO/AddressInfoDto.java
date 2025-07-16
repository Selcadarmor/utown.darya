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
    private String city;
    private String fullAddress;
}