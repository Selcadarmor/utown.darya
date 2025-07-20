package com.example.Utown.dto.dishDTO;

import com.example.Utown.dto.optionDTO.OptionInfoDto;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishCreateDto {
    private Integer sort;
    private Long dishCategoryId;
    private Long fileId;
    private String description;
    private Boolean isActive;
    private BigDecimal price;
    @JsonSetter(nulls = Nulls.SKIP)
    private List<OptionInfoDto> options = new ArrayList<>();
    private String title;
}
