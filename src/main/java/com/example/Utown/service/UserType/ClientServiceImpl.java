package com.example.Utown.service.UserType;

import com.example.Utown.dto.clientDto.ClientChangePasswordDto;
import com.example.Utown.dto.clientDto.ClientProfileUpdateDto;
import com.example.Utown.dto.clientDto.ClientRegistrationDto;
import com.example.Utown.exception.PasswordsDoNotMatchException;
import com.example.Utown.exception.RoleNotFoundException;
import com.example.Utown.exception.UserAlreadyExistsException;
import com.example.Utown.exception.UserNotFoundException;
import com.example.Utown.model.Role;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.repository.RoleRepository;
import com.example.Utown.repository.UserType.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

import static com.example.Utown.dto.mappers.ClientMapper.updateEntity;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<Client> findByUsername(String username) {
        return clientRepository.findByUsername(username);
    }

    @Override
    public void saveClient(ClientRegistrationDto dto, String roleName) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        if (clientRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(dto.getUsername());
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));

        Client client = new Client();
        client.setUsername(dto.getUsername());
        client.setPassword(passwordEncoder.encode(dto.getPassword()));
        client.setRoles(Set.of(role));

        clientRepository.save(client);
    }

    @Override
    public void updateClientProfile(String currentUsername, ClientProfileUpdateDto dto) {
        Client client = clientRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserNotFoundException(currentUsername));

        if (!client.getUsername().equals(dto.getUsername())
                && clientRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(dto.getUsername());
        }

        updateEntity(client, dto);
        clientRepository.save(client);
    }

    @Override
    public void changePassword(String username, ClientChangePasswordDto dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        Client client = clientRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        client.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        clientRepository.save(client);
    }


}

