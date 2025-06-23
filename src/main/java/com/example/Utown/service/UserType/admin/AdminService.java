package com.example.Utown.service.UserType.admin;

import com.example.Utown.dto.otherDto.ClientDto;
import java.util.List;

public interface AdminService {

    /// клиенты
    List<ClientDto> getAllClients();
    ClientDto getClientById(Long id);
}
