package com.example.Utown.dto.clientDTO;

import lombok.Data;

import java.util.Set;


@Data
public class ClientInfoDto {
    private Long id;
    private String fullName;
    private String username;
    private Set<Long> addressIds;
    private Integer orderCount;
    private Long fileInfoId;

    public ClientInfoDto(Long id, String fullName, String username, Set<Long> addressIds, Integer orderCount, Long fileInfoId) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.addressIds = addressIds;
        this.orderCount = orderCount;
        this.fileInfoId = fileInfoId;
    }

}
