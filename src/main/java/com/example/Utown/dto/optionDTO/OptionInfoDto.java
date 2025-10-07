package com.example.Utown.dto.optionDTO;

import com.example.Utown.dto.elementDTO.ElementInfoDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionInfoDto {
    private Long id;
    @NotBlank
    private String name;
    private Boolean required;
    private Integer min;
    private Integer max;
    private Set<ElementInfoDto> elements;
}

