package com.example.Utown.service;

import com.example.Utown.dto.fileInfoDTO.FileInfoDetailsDto;
import com.example.Utown.dto.fileInfoDTO.FileInfoDto;
import com.example.Utown.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileInfoService {
    FileInfo create(FileInfoDto dto);
    FileInfoDto getById(Long id);
    List<FileInfoDto> getAll();
    FileInfo update(Long id, FileInfoDto dto);
    void delete(Long id);
    FileInfo getFileInfoById(Long id);
    FileInfoDetailsDto saveFile(MultipartFile file);
    byte[] getFileBytes(Long id);
    FileInfoDetailsDto getFileInfo(Long id);
    FileInfo getFileInfoEntity(Long id);
}

