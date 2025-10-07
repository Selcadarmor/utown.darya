package com.example.Utown.service;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.model.Address;
import com.example.Utown.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final KafkaTemplate<Object, AddressDto> kafkaTemplate;

    @Override
    public Address createAddress(AddressDto dto) {
        dto.setRequestId(UUID.randomUUID().toString());
        // Формируем временный объект адреса
        Address tempAddress = new Address();
        tempAddress.setStreet(dto.getStreet());
        tempAddress.setCity(dto.getCity());
        tempAddress.setDetails(dto.getDetails());
        tempAddress.setIntercomCode(dto.getIntercomCode());

        // Временно заполняем обязательные поля
        tempAddress.setArea(""); // временно пустое, потом обновим из Kakao
        tempAddress.setState(""); // временно пустое
        tempAddress.setPostCode(""); // временно пустое
        tempAddress.setTypeAddress(0); // временно 0
        tempAddress.setFullAddress(""); // временно пустое
        tempAddress.setLatitude(0.0f); // временно 0
        tempAddress.setLongitude(0.0f); // временно 0

        Address savedAddress = addressRepository.save(tempAddress);
        dto.setAddressId(savedAddress.getId());

        // Отправляем в Kafka
        kafkaTemplate.send("location-requests", dto);
        log.info("Sent address request to Kafka: {}", dto);

        // Возвращаем временный объект владельцу
        return tempAddress;
    }



    @Override
    @Transactional(readOnly = true)
    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> {
                    return new ResourceNotFoundException("Address", id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Address updateAddress(Long id, AddressDto dto) {
        Address address = getAddressById(id);

        // Обновляем поля
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setDetails(dto.getDetails());
        address.setIntercomCode(dto.getIntercomCode());

        // Отправляем изменения в Kafka для Kakao API
        kafkaTemplate.send("location-update-requests", dto);

        // Возвращаем временный объект (еще не подтвержденный API)
        return address;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteAddress(Long id) {
        Address address = getAddressById(id);
        addressRepository.delete(address);
        log.info("Deleted address with id: {}", id);
    }
}
