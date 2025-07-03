package com.example.Utown.dto.fileInfoDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoDetailsDto {
    private  Long id;

    private String originalTitle;

    private String path;

    private String type;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
