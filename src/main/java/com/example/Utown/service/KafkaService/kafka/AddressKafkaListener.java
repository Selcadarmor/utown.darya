package com.example.Utown.service.KafkaService.kafka;

import com.example.Utown.dto.addressDTO.AddressResponseDto;
import com.example.Utown.model.Address;
import com.example.Utown.repository.AddressRepository;
import com.example.Utown.repository.UserType.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressKafkaListener {
    private final AddressRepository addressRepository;
    private final ClientRepository clientRepository;

    @KafkaListener(topics = "location-responses", groupId = "utown-group")
    public void handleAddressResponse(String message) {
        try {
            log.info("Received raw message from Kakao: {}", message);

            ObjectMapper objectMapper = new ObjectMapper();
            AddressResponseDto responseDto = objectMapper.readValue(message, AddressResponseDto.class);

            log.info("Parsed address response: {}", responseDto);

            // Ищем адрес по requestId или другим полям
            Optional<Address> optionalAddress = addressRepository
                    .findFirstByStreetAndCityAndDetailsAndIntercomCodeAndFullAddressIsNull(
                            responseDto.getStreet(),
                            responseDto.getCity(),
                            responseDto.getDetails(),
                            responseDto.getIntercomCode()
                    );

            if (optionalAddress.isPresent()) {
                Address address = optionalAddress.get();
                // Обновляем ВСЕ данные из Kakao API
                address.setArea(responseDto.getArea()); // ← обновляем area
                address.setState(responseDto.getState());
                address.setPostCode(responseDto.getPostCode());
                address.setTypeAddress(responseDto.getTypeAddress());
                address.setLatitude(responseDto.getLatitude());
                address.setLongitude(responseDto.getLongitude());
                address.setFullAddress(responseDto.getFullAddress());

                addressRepository.save(address);
                log.info("Address updated after Kakao API response: {}", address.getId());
            } else {
                log.warn("No pending address found for response: {}", responseDto);
            }
        } catch (Exception e) {
            log.error("Error processing address response. Raw message: {}", message, e);
        }
    }
}