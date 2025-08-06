package com.example.Utown.service.S3Service;

import com.example.Utown.config.S3.AwsProperties;
import com.example.Utown.dto.fileInfoDTO.FileInfoDto;
import com.example.Utown.exception.InvalidArgumentException;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.FileInfoMapper;
import com.example.Utown.model.FileInfo;
import com.example.Utown.repository.FileInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileInfoServiceImpl implements FileInfoService {

    private final FileInfoRepository fileInfoRepository;
    private final FileInfoMapper fileInfoMapper;
    private final S3Service s3Service;
    private final AwsProperties awsProperties;

    @Override
    public FileInfoDto saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.error("Uploaded file is null or empty");
            throw new IllegalArgumentException("File must not be null or empty");
        }

        log.info("Uploading file: {}", file.getOriginalFilename());
        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
        s3Service.uploadFile(file, key);

        FileInfo fileInfo = FileInfo.builder()
                .originalTitle(file.getOriginalFilename())
                .path(key)
                .type(file.getContentType())
                .build();
        FileInfo saved = fileInfoRepository.save(fileInfo);
        log.info("File uploaded successfully: {}", saved.getId());
        return fileInfoMapper.toDtoFile(saved, awsProperties);
    }

    @Override
    public FileInfoDto getFileInfo(Long id) {
        if (id == null) {
            log.error("File id is null");
            throw new InvalidArgumentException("fileId", "null");
        }
        log.info("Getting file info for id: {}", id);
        FileInfo fileInfo = findById(id);
        log.info("File info for id: {} found", id);
        return fileInfoMapper.toDtoFile(fileInfo, awsProperties);
    }


    @Override
    public void delete(Long id) {
        if (id == null) {
            log.error("File id is null");
            throw new InvalidArgumentException("fileId", "null");
        }
        FileInfo file = findById(id);
        log.info("Deleting file with id: {}", id);
        fileInfoRepository.delete(file);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public FileInfo getFileInfoById(Long id) {
        if (id == null) {
            log.error("File id is null");
            throw new InvalidArgumentException("fileId", "null");
        }
        return fileInfoRepository.findById(id)
                .orElse( null);
    }
    @Override
    public FileInfo findById(Long id) {
        if (id == null) {
            log.error("File id is null");
            throw new InvalidArgumentException("fileId", "null");
        }
        return fileInfoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File", id));
    }

}

