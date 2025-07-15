package com.example.Utown.dto.clientDTO;

import com.example.Utown.dto.addressDTO.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ClientProfileUpdateDto {
    private String fullName;
    private AddressDto addressDto;
}






