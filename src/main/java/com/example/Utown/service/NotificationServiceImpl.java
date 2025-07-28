package com.example.Utown.service;

import com.example.Utown.dto.notificationDTO.NotificationDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.NotificationMapper;
import com.example.Utown.model.Notification;
import com.example.Utown.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    @Override
    public Notification getById(Long id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification" , id));
        return notification;
    }

    @Override
    public Notification create(NotificationDto dto) {
        return repository.save(mapper.toEntity(dto));
    }

    @Override
    public List<Notification> getAll() {
        return repository.findAll();
    }

    @Override
    public Notification update(Long id, NotificationDto dto) {
        Notification entity = getById(id);

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
        Notification notification = getById(id);
        repository.delete(notification);
    }
}

