package com.example.Utown.dto.optionDTO;

import com.example.Utown.dto.elementDTO.ElementInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionInfoDto {
    private Long id;
    private String name;
    private Boolean required;
    private Integer min;
    private Integer max;
    private Boolean isActive;
    private List<ElementInfoDto> elements;
}

