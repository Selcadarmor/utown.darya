package com.example.Utown.service;

import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import java.util.List;
import com.example.Utown.model.Delivery;

public interface DeliveryService {
    Delivery createDelivery(DeliveryDto dto);
    DeliveryDto getDeliveryById(Long id);
    List<DeliveryDto> getAllDeliveries();
    Delivery updateDelivery(Long id, DeliveryDto dto);
    void deleteDelivery(Long id);
}


