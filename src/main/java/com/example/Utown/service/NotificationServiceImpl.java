package com.example.Utown.service;

import com.example.Utown.dto.notificationDTO.NotificationDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.NotificationMapper;
import com.example.Utown.model.Notification;
import com.example.Utown.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    @Override
    public Notification create(NotificationDto dto) {
        return repository.save(mapper.toEntity(dto));
    }

    @Override
    public NotificationDto getById(Long id) {
        Notification entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found", id));
        return mapper.toDto(entity);
    }

    @Override
    public List<NotificationDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Notification update(Long id, NotificationDto dto) {
        Notification entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found", id));

        entity.setDate(dto.getDate());
        entity.setText(dto.getText());
        entity.setTime(dto.getTime());
        entity.setTitle(dto.getTitle());
        entity.setErrorMessage(dto.getErrorMessage());
        entity.setIsSuccessful(dto.getIsSuccessful());

        return repository.save(entity);
    }

    @Override
    public void delete(Long id) {
        Notification entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found", id));
        repository.delete(entity);
    }
}

