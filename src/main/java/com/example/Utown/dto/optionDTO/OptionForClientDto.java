package com.example.Utown.dto.optionDTO;

import com.example.Utown.dto.elementDTO.ElementForClientDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionForClientDto {
    private Long id;
    private String name;
    private boolean required;
    private Integer min;
    private Integer max;
    private Boolean isActive;
    private Set<ElementForClientDto> elements;
}