package com.example.Utown.service;

import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.DeliveryMapper;
import com.example.Utown.model.Delivery;
import com.example.Utown.model.Restaurant;
import com.example.Utown.repository.DeliveryRepository;
import com.example.Utown.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final RestaurantRepository restaurantRepository;
    private final DeliveryMapper deliveryMapper;

    @Override
    public DeliveryDto createDelivery(DeliveryDto dto) {
        Delivery delivery = deliveryMapper.deliveryDtoToEntity(dto);

        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found", dto.getRestaurantId()));
        delivery.setRestaurant(restaurant);

        Delivery saved = deliveryRepository.save(delivery);
        return deliveryMapper.deliveryToDto(saved);
    }

    @Override
    public DeliveryDto getDeliveryById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found", id));
        return deliveryMapper.deliveryToDto(delivery);
    }

    @Override
    public List<DeliveryDto> getAllDeliveries() {
        return deliveryRepository.findAll()
                .stream()
                .map(deliveryMapper::deliveryToDto)
                .toList();
    }

    @Override
    public DeliveryDto updateDelivery(Long id, DeliveryDto dto) {
        Delivery existing = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found", id));

        existing.setArea(dto.getArea());
        existing.setPrice(dto.getPrice());
        existing.setDistrict(dto.getDistrict());
        existing.setIsActive(dto.getIsActive());
        existing.setIsDeleted(dto.getIsDeleted());

        if (dto.getRestaurantId() != null) {
            Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found", dto.getRestaurantId()));
            existing.setRestaurant(restaurant);
        }

        Delivery updated = deliveryRepository.save(existing);
        return deliveryMapper.deliveryToDto(updated);
    }

    @Override
    public void deleteDelivery(Long id) {
        if (!deliveryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Delivery not found", id);
        }
        deliveryRepository.deleteById(id);
    }
}
