package com.example.Utown.dto.dishCategoryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishCategoryCreateDto {
    private String name;
    private Integer sort;
    private Long fileInfoId;
}
