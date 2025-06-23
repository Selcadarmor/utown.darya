package com.example.Utown.service.UserType.admin;

import com.example.Utown.dto.ClientCreateDto;
import com.example.Utown.dto.ClientDto;
import com.example.Utown.dto.ClientUpdateDto;
import com.example.Utown.dto.adminDto.AdminCreateRequestDto;
import com.example.Utown.dto.adminDto.AdminDto;
import com.example.Utown.dto.adminDto.AdminUpdateRequestDto;

import java.util.List;

public interface AdminService {

    /// клиенты
    List<ClientDto> getAllClients();
    ClientDto getClientById(Long id);
    ClientDto createClient(ClientCreateDto clientCreateDto);
    ClientDto updateClient(Long id, ClientUpdateDto clientDto);
    void deleteClient(Long id);
    /// администраторы
    List<AdminDto> getAllAdmins();
    AdminDto getAdminById(Long id);
    AdminDto createAdmin(AdminCreateRequestDto dto);
    AdminDto updateAdmin(Long id, AdminUpdateRequestDto dto);
    void deleteAdmin(Long id);
}
