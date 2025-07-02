package com.example.Utown.dto.clientDto;

import com.example.Utown.model.Address;
import com.example.Utown.model.FileInfo;
import lombok.Data;

import java.util.Set;


@Data
public class ClientInfoDto {
    private Long id;
    private String fullName;
    private String username;
    private Set<Address> addresses;
    private Integer orderCount;
    private FileInfo fileInfo;

    public ClientInfoDto(Long id, String fullName, String username, Set<Address> addresses, Integer orderCount, FileInfo fileInfoId) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.addresses = addresses;
        this.orderCount = orderCount;
        this.fileInfo = fileInfo;
    }

}
