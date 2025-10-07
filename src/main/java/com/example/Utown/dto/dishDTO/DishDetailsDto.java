package com.example.Utown.dto.dishDTO;

import com.example.Utown.dto.optionDTO.OptionInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishDetailsDto {
    private String description;
    private Boolean isActive;
    private BigDecimal price;
    private Integer sort;
    private String title;
    private Long dishCategoryId;
    private Set<OptionInfoDto> options;

}
