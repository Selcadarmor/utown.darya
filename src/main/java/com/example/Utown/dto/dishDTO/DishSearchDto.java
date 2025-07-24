package com.example.Utown.dto.dishDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishSearchDto {
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
}
