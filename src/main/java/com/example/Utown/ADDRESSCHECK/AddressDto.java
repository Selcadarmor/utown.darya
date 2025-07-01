package com.example.Utown.ADDRESSCHECK;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
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
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
}



