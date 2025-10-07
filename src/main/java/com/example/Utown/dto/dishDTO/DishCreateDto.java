package com.example.Utown.dto.dishDTO;

import com.example.Utown.dto.optionDTO.OptionInfoDto;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishCreateDto {
    private Integer sort;
    private Long dishCategoryId;
    private Long fileId;
    private String description;
    private Boolean isActive;
    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;
    private Set<OptionInfoDto> options = new HashSet<>();
    @NotBlank
    private String title;
}
