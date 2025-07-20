package com.example.Utown.dto.dishDTO;

import com.example.Utown.dto.optionDTO.OptionInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishInfoDto {
    private String description;
    private Boolean isActive;
    private BigDecimal price;
    private Integer sort;
    private String title;
    private Long dishCategoryId;
    private List<OptionInfoDto> options;
    private Long fileId;
}
