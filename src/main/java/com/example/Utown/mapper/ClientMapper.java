package com.example.Utown.mapper;

import com.example.Utown.dto.ClientCreateDto;
import com.example.Utown.dto.ClientDto;
import com.example.Utown.dto.ClientUpdateDto;
import com.example.Utown.model.UserType.Client;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = {
                RoleMapper.class,
                AddressMapper.class,
                RestaurantMapper.class
        }
)
public interface ClientMapper {
    //для ответа
    @Mapping(source = "address.city", target = "city")
    @Mapping(source = "address.fullAddress", target = "fullAddress")
    @Mapping(source = "orders", target = "orderHistory")
    @Mapping(target = "totalOrders", expression = "java(client.getOrders() !=null ? client.getOrders().size() : 0)")
    ClientDto toDto(Client client);
    //для создания
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Client toEntity(ClientCreateDto clientCreateDto);

    // Для обновления клиента
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateClientFromDto(ClientUpdateDto dto, @MappingTarget Client client);
}

