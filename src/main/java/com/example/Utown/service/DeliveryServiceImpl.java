package com.example.Utown.service;

import com.example.Utown.dto.deliveryDTO.DeliveryDto;
import com.example.Utown.dto.deliveryDTO.DeliveryInfoDto;
import com.example.Utown.exception.InvalidArgumentException;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.DeliveryMapper;
import com.example.Utown.model.Delivery;
import com.example.Utown.model.Restaurant;
import com.example.Utown.repository.DeliveryRepository;
import com.example.Utown.repository.RestaurantRepository;
import com.example.Utown.service.UserTypeService.RestaurantAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final RestaurantAdminService restaurantAdminService;
    private final RestaurantRepository restaurantRepository;

    @Override
    public Delivery getDeliveryById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Delivery not found with id: {}", id);
                    return new ResourceNotFoundException("Delivery", id);
                });
        log.info("Found delivery with id: {}", id);
        return delivery;
    }

    @Override
    public List<DeliveryDto> getAllDeliveriesByRestaurantId(Long restaurantId) {
        List<Delivery> deliveries = deliveryRepository.findByRestaurantId(restaurantId);
        log.info("Fetched {} deliveries for restaurantId: {}", deliveries.size(), restaurantId);
        return deliveryMapper.deliveryToDto(deliveries);
    }

    @Override
    public List<DeliveryDto> getAllInActiveDeliveriesByRestaurantId(Long restaurantId) {
        List<Delivery> deliveries = deliveryRepository.findActiveByRestaurantId(restaurantId);
        log.info("Fetched {} active deliveries for restaurantId: {}", deliveries.size(), restaurantId);
        return deliveryMapper.deliveryToDto(deliveries);
    }

    @Override
    public List<DeliveryDto> getAllDeletedDeliveriesByRestaurantId(Long restaurantId) {
        List<Delivery> deliveries = deliveryRepository.findDeletedByRestaurantId(restaurantId);
        log.info("Fetched {} deleted deliveries for restaurantId: {}", deliveries.size(), restaurantId);
        return deliveryMapper.deliveryToDto(deliveries);
    }

    @Override
    public List<DeliveryInfoDto> getDeliveriesByRestaurantId(Long restaurantId) {
        List<Delivery> deliveries = deliveryRepository.findByRestaurantId(restaurantId);
        log.info("Fetched delivery info list ({} entries) for restaurantId: {}", deliveries.size(), restaurantId);
        return deliveryMapper.toDtoList(deliveries);
    }

    @Override
    public Delivery createDelivery(DeliveryDto dto) {
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> {
                    log.warn("Restaurant not found with id: {}", dto.getRestaurantId());
                    return new ResourceNotFoundException("Restaurant", dto.getRestaurantId());
                });

        Delivery delivery = Delivery.builder()
                .area(dto.getArea())
                .price(dto.getPrice())
                .district(dto.getDistrict())
                .isActive(true)
                .isDeleted(false)
                .restaurant(restaurant)
                .build();

        Delivery saved = deliveryRepository.save(delivery);
        log.info("Created new delivery (id: {}) for restaurant: {}", saved.getId(), restaurant.getId());
        return saved;
    }

    @Override
    public Delivery addDelivery(DeliveryDto dto) {
        Restaurant restaurant = restaurantAdminService.getCurrentAdmin().getRestaurant();

        boolean exists = deliveryRepository.existsByRestaurantAndAreaAndDistrict(
                restaurant.getId(), dto.getArea(), dto.getDistrict());

        if (exists) {
            log.warn("Duplicate delivery exists for restaurant {} with area: {}, district: {}",
                    restaurant.getId(), dto.getArea(), dto.getDistrict());
            throw new IllegalArgumentException("Delivery with this area and district already exists for the restaurant");
        }

        Delivery delivery = Delivery.builder()
                .area(dto.getArea())
                .price(dto.getPrice())
                .district(dto.getDistrict())
                .isActive(true)
                .isDeleted(false)
                .restaurant(restaurant)
                .build();

        Delivery saved = deliveryRepository.save(delivery);
        log.info("Added delivery (id: {}) for restaurant: {}", saved.getId(), restaurant.getId());
        return saved;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateDeliveriesByRestaurant(Restaurant restaurant, List<DeliveryDto> dtos) {
        Map<Long, Delivery> existingDeliveriesById = restaurant.getDeliveries().stream()
                .filter(d -> d.getId() != null)
                .collect(Collectors.toMap(Delivery::getId, Function.identity()));

        for (DeliveryDto dto : dtos) {
            Long id = dto.getId();
            if (id == null) {
                log.warn("Received DTO with null deliveryId");
                throw new InvalidArgumentException("deliveryId", null);
            }
            Delivery delivery = existingDeliveriesById.get(id);
            if (delivery == null) {
                log.warn("Delivery not found in restaurant for id: {}", id);
                throw new ResourceNotFoundException("Delivery", id);
            }

            delivery.setArea(dto.getArea());
            delivery.setDistrict(dto.getDistrict());
            delivery.setPrice(dto.getPrice());
            delivery.setIsActive(true);
            delivery.setIsDeleted(false);
        }

        deliveryRepository.saveAll(existingDeliveriesById.values());
        log.info("Updated {} deliveries for restaurant: {}", dtos.size(), restaurant.getId());
    }

    @Override
    public Delivery updateDelivery(Long id, DeliveryDto dto) {
        Delivery delivery = getDeliveryById(id);

        Restaurant restaurant = restaurantAdminService.getCurrentAdmin().getRestaurant();

        boolean exists = deliveryRepository.existsSimilarDelivery(
                restaurant.getId(), dto.getArea(), dto.getDistrict(), id);

        if (exists) {
            log.warn("Attempt to update delivery to a duplicate area/district for restaurant: {}", restaurant.getId());
            throw new IllegalArgumentException("Delivery with this area and district already exists for the restaurant");
        }

        delivery.setArea(dto.getArea());
        delivery.setPrice(dto.getPrice());
        delivery.setDistrict(dto.getDistrict());
        delivery.setIsActive(true);
        delivery.setIsDeleted(false);

        Delivery updated = deliveryRepository.save(delivery);
        log.info("Updated delivery with id: {}", updated.getId());
        return updated;
    }

    @Override
    @Transactional
    public void reactivateDelivery(Long id) {
        Delivery delivery = getDeliveryById(id);
        delivery.setIsActive(true);
        delivery.setIsDeleted(false);
        deliveryRepository.save(delivery);
        log.info("Reactivated delivery with id: {}", id);
    }

    @Override
    @Transactional
    public void stopDelivery(Long id) {
        Delivery delivery = getDeliveryById(id);
        delivery.setIsActive(false);
        deliveryRepository.save(delivery);
        log.info("Stopped delivery with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteDelivery(Long id) {
        Delivery delivery = getDeliveryById(id);
        delivery.setIsActive(false);
        delivery.setIsDeleted(true);
        deliveryRepository.save(delivery);
        log.info("Deleted delivery with id: {}", id);
    }

}

