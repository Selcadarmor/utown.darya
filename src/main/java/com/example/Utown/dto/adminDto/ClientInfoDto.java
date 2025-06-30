package com.example.Utown.dto.adminDto;

import lombok.Data;


@Data
public class ClientInfoDto {
    private Long id;
    private String fullName;
    private String username;
    private String fullAddress;
    private String city;
    private Long orderCount;
    private Long fileInfoId;



    public ClientInfoDto(Long id, String fullName, String username, String fullAddress, String city, Long orderCount) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.fullAddress = fullAddress;
        this.city = city;
        this.orderCount = orderCount;

    }
    public ClientInfoDto(Long id, String fullName, String username, String fullAddress, Long orderCount, Long fileInfoId) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.fullAddress = fullAddress;
        this.orderCount = orderCount;
        this.fileInfoId = fileInfoId;
    }
}
