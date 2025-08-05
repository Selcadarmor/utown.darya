package com.example.Utown.service.S3Service;

import com.example.Utown.config.S3.AwsProperties;
import com.example.Utown.dto.fileInfoDTO.FileInfoDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.FileInfoMapper;
import com.example.Utown.model.FileInfo;
import com.example.Utown.repository.FileInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileInfoServiceImpl implements FileInfoService {

    private final FileInfoRepository fileInfoRepository;
    private final FileInfoMapper fileInfoMapper;
    private final S3Service s3Service;
    private final AwsProperties awsProperties;

    @Override
    public FileInfoDto saveFile(MultipartFile file) {
        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
        s3Service.uploadFile(file, key);

        FileInfo fileInfo = FileInfo.builder()
                .originalTitle(file.getOriginalFilename())
                .path(key)
                .type(file.getContentType())
                .build();
        FileInfo saved = fileInfoRepository.save(fileInfo);
        return fileInfoMapper.toDtoFile(saved, awsProperties);

    }

    @Override
    public FileInfoDto getFileInfo(Long id) {
        FileInfo fileInfo = findById(id);
        return fileInfoMapper.toDtoFile(fileInfo, awsProperties);
    }

    @Override
    public FileInfo create(FileInfoDto dto) {
        FileInfo fileInfo = fileInfoMapper.toEntity(dto);
        return fileInfoRepository.save(fileInfo);
    }

    @Override
    public FileInfo update(Long id, FileInfoDto dto) {
        FileInfo file = findById(id);
        file.setOriginalTitle(dto.getOriginalTitle());
        file.setPath(dto.getPath());
        file.setType(dto.getType());

        return fileInfoRepository.save(file);
    }

    @Override
    public void delete(Long id) {
        FileInfo file = findById(id);
        fileInfoRepository.delete(file);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public FileInfo getFileInfoById(Long id) {
        return fileInfoRepository.findById(id)
                .orElse( null);
    }
    @Override
    public FileInfo findById(Long id) {
        return fileInfoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File", id));
    }

}

