package com.example.Utown.dto.restaurantCategoryDTO;

import com.example.Utown.dto.fileInfoDTO.FileInfoDetailsDto;
import com.example.Utown.dto.fileInfoDTO.FileInfoDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantCategoryDto {
    private Long id;
    @NotBlank
    private String name;
    private Integer sort;
    private Boolean isActive;
    private FileInfoDto file;
}
