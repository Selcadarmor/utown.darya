package com.example.Utown.dto.addressDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private String requestId;
    private Long addressId;
    private String street;         // улица
    private String city;           // город
    private String details;        // квартира, подъезд и т.д.
    private String intercomCode;// код домофона
    private Long clientId;
}

