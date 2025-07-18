package com.example.Utown.dto.elementDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElementForClientDto {
    private Long id;
    private String name;
    private Boolean isActive;
}

