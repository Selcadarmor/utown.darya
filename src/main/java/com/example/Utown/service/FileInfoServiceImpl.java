package com.example.Utown.service;

import com.example.Utown.dto.fileInfoDTO.FileInfoDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.FileInfoMapper;
import com.example.Utown.model.FileInfo;
import com.example.Utown.repository.FileInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileInfoServiceImpl implements FileInfoService {

    private final FileInfoRepository fileInfoRepository;
    private final FileInfoMapper fileInfoMapper;

    @Override
    public FileInfo create(FileInfoDto dto) {
        FileInfo fileInfo = fileInfoMapper.toEntity(dto);
        return fileInfoRepository.save(fileInfo);
    }

    @Override
    public FileInfoDto getById(Long id) {
        FileInfo file = fileInfoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FileInfo not found", id));
        return fileInfoMapper.toDto(file);
    }

    @Override
    public List<FileInfoDto> getAll() {
        return fileInfoRepository.findAll().stream()
                .map(fileInfoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public FileInfo update(Long id, FileInfoDto dto) {
        FileInfo file = fileInfoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FileInfo not found", id));

        file.setOriginalTitle(dto.getOriginalTitle());
        file.setPath(dto.getPath());
        file.setType(dto.getType());

        return fileInfoRepository.save(file);
    }

    @Override
    public void delete(Long id) {
        FileInfo file = fileInfoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FileInfo not found", id));
        fileInfoRepository.delete(file);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public FileInfo getFileInfoById(Long id) {
        return fileInfoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FileInfo not found", id));
    }
}

