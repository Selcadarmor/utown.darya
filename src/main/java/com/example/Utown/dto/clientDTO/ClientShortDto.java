package com.example.Utown.dto.clientDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientShortDto {
    private Long id;
    private String fullName;
    private String phoneNumber;
}