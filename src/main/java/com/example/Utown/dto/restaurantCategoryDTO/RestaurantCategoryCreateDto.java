package com.example.Utown.dto.restaurantCategoryDTO;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantCategoryCreateDto {
    @NotBlank
    private String name;
    private Integer sort;
    private Boolean isActive;
    private Long fileId;
}

