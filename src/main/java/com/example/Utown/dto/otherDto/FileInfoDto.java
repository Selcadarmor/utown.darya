package com.example.Utown.dto.otherDto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoDto {
    private Long id;
    private String originalTitle;
    private String path;
    private String type;
}
