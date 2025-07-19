package com.example.Utown.dto.clientDTO;

import com.example.Utown.dto.addressDTO.AddressInfoDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientDetailsDto {
    private String fullName;
    private String username;
    private String city;
    private String fullAddress;
    private Integer orderCount;

    public ClientDetailsDto(String fullName, String username, String city, String fullAddress, Integer orderCount) {
        this.fullName = fullName;
        this.username = username;
        this.city = city;
        this.fullAddress = fullAddress;
        this.orderCount = orderCount;
    }
}

