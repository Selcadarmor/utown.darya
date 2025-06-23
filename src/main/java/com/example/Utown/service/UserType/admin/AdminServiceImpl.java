package com.example.Utown.service.UserType.admin;

import com.example.Utown.dto.otherDto.ClientDto;
import com.example.Utown.exception.IllegalArgumentException;
import com.example.Utown.mapper.AdminMapper;
import com.example.Utown.mapper.ClientMapper;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.repository.UserType.AdminRepository;
import com.example.Utown.repository.UserType.ClientRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private  final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public List<ClientDto> getAllClients() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDto)
                .toList();
    }

    @Override
    public ClientDto getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id));
        return clientMapper.toDto(client);
    }
}
