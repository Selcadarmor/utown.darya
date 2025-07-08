package com.example.Utown.dto.clientDTO;

import com.example.Utown.dto.addressDTO.AddressDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClientProfileUpdateDto {
    private String fullName;
    private List<AddressDto> addresses;
}






