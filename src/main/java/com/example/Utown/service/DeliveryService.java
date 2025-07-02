package com.example.Utown.service;

import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import java.util.List;

public interface DeliveryService {
    DeliveryDto createDelivery(DeliveryDto dto);
    DeliveryDto getDeliveryById(Long id);
    List<DeliveryDto> getAllDeliveries();
    DeliveryDto updateDelivery(Long id, DeliveryDto dto);
    void deleteDelivery(Long id);
}

