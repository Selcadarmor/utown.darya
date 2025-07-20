package com.example.Utown.dto.optionDTO;

import com.example.Utown.dto.elementDTO.ElementInfoDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
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
    private List<ElementInfoDto> elements;

}

