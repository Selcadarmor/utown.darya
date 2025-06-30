package com.example.Utown.dto.adminDto;

import lombok.Builder;
import lombok.Data;


@Data
public class ClientInfoDto {
    private Long id;
    private String fullName;
    private String username;
    private String fullAddress;
    private String city;
    private Long orderCount;
    private String phone;

    @Builder
    public ClientInfoDto(Long id, String fullName, String username, String fullAddress, String city, Long orderCount, String phone) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.fullAddress = fullAddress;
        this.city = city;
        this.orderCount = orderCount;
        this.phone = phone;
    }
}
