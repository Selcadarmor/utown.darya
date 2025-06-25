package com.example.Utown.service.UserType.admin;


import com.example.Utown.dto.ClientDto;
import com.example.Utown.dto.ClientUpdateDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.AdminMapper;
import com.example.Utown.mapper.ClientMapper;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.repository.RoleRepository;
import com.example.Utown.repository.UserType.AdminRepository;
import com.example.Utown.repository.UserType.ClientRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private  final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final RoleRepository roleRepository;

    @Override
    public List<ClientDto> getAllClients() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDto)
                .toList();
    }

    @Override
    public ClientDto getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
        return clientMapper.toDto(client);
    }

    @Transactional
    @Override
    public ClientDto updateClient(Long id, ClientUpdateDto clientDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
            clientMapper.updateClientFromDto(clientDto, client);
        return clientMapper.toDto(clientRepository.save(client));
    }

    @Transactional
    @Override
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
        clientRepository.delete(client);
    }
}
