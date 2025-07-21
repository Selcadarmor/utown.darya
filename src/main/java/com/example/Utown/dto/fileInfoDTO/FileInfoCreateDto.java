package com.example.Utown.dto.fileInfoDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoCreateDto {
    private String originalTitle;
    private String path;
    private String type;
}
