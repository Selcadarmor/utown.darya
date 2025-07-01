package com.example.Utown.service.UserType.client;

import com.example.Utown.dto.adminDto.ClientInfoDto;
import com.example.Utown.dto.adminDto.ClientUpdateDto;
import com.example.Utown.dto.adminDto.OrderShortDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.OperatingModeMapper;
import com.example.Utown.model.UserType.Client;
import com.example.Utown.repository.OrderRepository;
import com.example.Utown.repository.UserType.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;


    @Override
    public Optional<Client> findByUsername(String username) {
        return clientRepository.findByUsername(username);
    }


    @Override
    public List<ClientInfoDto> getAllClients() {
        return clientRepository.findAllClientInfos();
    }

    @Override
    public ClientInfoDto getClientById(Long id) {
        return clientRepository.findAllClientInfoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    @Transactional
    @Override
    public ClientInfoDto updateClient(Long id, ClientUpdateDto clientDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
        client.setFullName(clientDto.getFullName());
        client.setUsername(clientDto.getUsername());
        client.setActive(clientDto.isActive());

        clientRepository.save(client);

        return clientRepository.findAllClientInfoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    @Transactional
    @Override
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client", id);
        }
        clientRepository.deleteById(id);
    }

    @Override
    public List<OrderShortDto> getClientOrders(Long clientId) {
        return orderRepository.findAllOrdersByClientId(clientId);
    }

}

