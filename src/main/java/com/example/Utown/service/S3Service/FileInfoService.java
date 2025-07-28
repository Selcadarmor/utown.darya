package com.example.Utown.service.S3Service;

import com.example.Utown.dto.fileInfoDTO.FileInfoDetailsDto;
import com.example.Utown.dto.fileInfoDTO.FileInfoDto;
import com.example.Utown.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileInfoService {
    FileInfo create(FileInfoDto dto);
    FileInfo findById(Long id);
    FileInfo update(Long id, FileInfoDto dto);
    void delete(Long id);
    FileInfo getFileInfoById(Long id);
    FileInfoDetailsDto saveFile(MultipartFile file);
    byte[] getFileBytes(Long id);
    FileInfoDetailsDto getFileInfo(Long id);
}

