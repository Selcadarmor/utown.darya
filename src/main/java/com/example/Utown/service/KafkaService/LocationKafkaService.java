package com.example.Utown.service.KafkaService;

import com.example.Utown.dto.LocationRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationKafkaService {

    private final KafkaTemplate<String, LocationRequestDto> kafkaTemplate;

    private static final String LOCATION_REQUEST_TOPIC = "location-requests";

    public void send(LocationRequestDto requestDto) {
        kafkaTemplate.send(LOCATION_REQUEST_TOPIC, requestDto);
        log.info("Sent location request to Kakao: {}", requestDto);
    }

}