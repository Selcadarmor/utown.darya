package com.example.Utown.mapper;

import com.example.Utown.dto.ClientDto;
import com.example.Utown.model.UserType.Client;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {
                RoleMapper.class,
                AddressMapper.class,
                RestaurantMapper.class
        }
)
public interface ClientMapper {
    ClientDto toDto(Client client);
    Client toEntity(ClientDto dto);
}
