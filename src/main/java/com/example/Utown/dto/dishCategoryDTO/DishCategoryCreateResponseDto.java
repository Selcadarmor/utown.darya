package com.example.Utown.dto.dishCategoryDTO;

import com.example.Utown.dto.fileInfoDTO.FileInfoDto;
import com.example.Utown.model.FileInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishCategoryCreateResponseDto {
    private String name;
    private Integer sort;
    private FileInfoDto file;
}
