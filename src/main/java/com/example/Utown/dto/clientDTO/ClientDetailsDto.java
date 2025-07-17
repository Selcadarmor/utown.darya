package com.example.Utown.dto.clientDTO;

import com.example.Utown.dto.addressDTO.AddressInfoDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientDetailsDto {
    private Long id;
    private String fullName;
    private String username;
    private AddressInfoDto defaultAddress;
    private Integer orderCount;

    public ClientDetailsDto(Long id, String fullName, String username, AddressInfoDto defaultAddress, Integer orderCount) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.defaultAddress = defaultAddress;
        this.orderCount = orderCount;
    }
}

