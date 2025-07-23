package com.example.Utown.dto.dishDTO;

import com.example.Utown.dto.optionDTO.OptionForClientDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishForClientDto {
    private Long id;
    private String title;
    private String description;
    private Boolean isActive;
    private Boolean isDeleted;
    private BigDecimal price;
    private Integer sort;
    private Long restaurantId;
    private Long dishCategoryId;
    private String filePath;
    private Set<OptionForClientDto> options;
}