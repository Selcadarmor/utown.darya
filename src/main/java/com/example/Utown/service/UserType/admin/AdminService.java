package com.example.Utown.service.UserType.admin;

import com.example.Utown.dto.ClientDto;
import com.example.Utown.dto.ClientUpdateDto;

import java.util.List;

public interface AdminService {

    /// клиенты
    List<ClientDto> getAllClients();
    ClientDto getClientById(Long id);
    ClientDto updateClient(Long id, ClientUpdateDto clientDto);
    void deleteClient(Long id);

}
