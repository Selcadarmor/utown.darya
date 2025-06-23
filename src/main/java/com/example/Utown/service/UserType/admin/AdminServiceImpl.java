package com.example.Utown.service.UserType.admin;

import com.example.Utown.dto.ClientCreateDto;
import com.example.Utown.dto.ClientDto;
import com.example.Utown.dto.ClientUpdateDto;
import com.example.Utown.dto.RoleDto;
import com.example.Utown.dto.adminDto.AdminCreateRequestDto;
import com.example.Utown.dto.adminDto.AdminDto;
import com.example.Utown.dto.adminDto.AdminUpdateRequestDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.exception.InvalidArgumentException;
import com.example.Utown.mapper.AdminMapper;
import com.example.Utown.mapper.ClientMapper;
import com.example.Utown.model.Role;
import com.example.Utown.model.UserType.Admin;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.repository.RoleRepository;
import com.example.Utown.repository.UserType.AdminRepository;
import com.example.Utown.repository.UserType.ClientRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public ClientDto createClient(ClientCreateDto clientCreateDto) {
        if (clientCreateDto.getRoles() == null || clientCreateDto.getRoles().isEmpty()) {
            throw new InvalidArgumentException("roles", clientCreateDto.getRoles());
        }
        Client client = clientMapper.toEntity(clientCreateDto);

        Set<Long> roleIds = clientCreateDto.getRoles().stream()
                .map(RoleDto::getId)
                .collect(Collectors.toSet());
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
        if (roles.size() != roleIds.size()) {
            throw new ResourceNotFoundException("Role", clientCreateDto.getRoles());
        }
        client.setRoles(roles);

        return clientMapper.toDto(clientRepository.save(client));
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

    @Override
    public List<AdminDto> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(adminMapper::toDto)
                .toList();
    }

    @Override
    public AdminDto getAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin", id));
        return adminMapper.toDto(admin);
    }

    @Transactional
    @Override
    public AdminDto createAdmin(AdminCreateRequestDto adminDto) {
        if (adminDto.getRoles() == null || adminDto.getRoles().isEmpty()) {
            throw new InvalidArgumentException("roles", adminDto.getRoles());
        }
        Admin admin = adminMapper.toAdmin(adminDto);

        Set<Long> roleIds = adminDto.getRoles().stream()
                .map(RoleDto::getId)
                .collect(Collectors.toSet());
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
        if (roles.size() != roleIds.size()) {
            throw new ResourceNotFoundException("Role", adminDto.getRoles());
        }
        admin.setRoles(roles);

        return adminMapper.toDto(adminRepository.save(admin));
    }

    @Transactional
    @Override
    public AdminDto updateAdmin(Long id, AdminUpdateRequestDto adminDto) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin", id));
        adminMapper.updateAdmin(adminDto, admin);
        return adminMapper.toDto(adminRepository.save(admin));
    }

    @Transactional
    @Override
    public void deleteAdmin(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin", id));
        adminRepository.delete(admin);
    }
}
