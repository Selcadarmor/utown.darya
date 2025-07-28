package com.example.Utown.dto.clientDTO;

import lombok.Data;

@Data
public class ClientInfoDto {
    private String fullName;
    private String username;
    private String city;
    private String fullAddress;
    private Integer orderCount;
    private Long fileId;
    private String path;

    public ClientInfoDto(String fullName, String username, String city, String fullAddress, Integer orderCount, Long fileId, String path) {
        this.fullName = fullName;
        this.username = username;
        this.city = city;
        this.fullAddress = fullAddress;
        this.orderCount = orderCount;
        this.fileId = fileId;
        this.path = path;
    }

}
