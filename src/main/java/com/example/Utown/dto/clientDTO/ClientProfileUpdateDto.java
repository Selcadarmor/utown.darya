package com.example.Utown.dto.clientDTO;

import com.example.Utown.dto.addressDTO.AddressDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClientProfileUpdateDto {
    private Long id;
    private String fullName;
    private List<AddressDto> addresses; // заменить одиночный AddressDto на список
}






