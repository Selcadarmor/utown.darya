package com.example.Utown.dto.dishCategoryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishCategoryCreateResponseDto {
    private String name;
    private Integer sort;
    private Long fileId;
}
