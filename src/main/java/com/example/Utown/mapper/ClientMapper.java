package com.example.Utown.mapper;

import com.example.Utown.dto.ClientDto;
import com.example.Utown.dto.ClientUpdateDto;
import com.example.Utown.model.UserType.Client;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDto toDto(Client client);

    // Для обновления клиента
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateClientFromDto(ClientUpdateDto dto, @MappingTarget Client client);
}

