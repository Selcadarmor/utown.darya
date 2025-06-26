package com.example.Utown.service.UserType.admin;

import com.example.Utown.dto.adminDto.ClientInfoDto;
import com.example.Utown.dto.adminDto.ClientUpdateDto;
import com.example.Utown.dto.adminDto.OrderShortDto;

import java.util.List;

public interface AdminService {

    List<ClientInfoDto> getAllClients();
    ClientInfoDto getClientById(Long id);
    ClientInfoDto updateClient(Long id, ClientUpdateDto dto);
    void deleteClient(Long id);
    List<OrderShortDto> getClientOrders(Long clientId);
}
