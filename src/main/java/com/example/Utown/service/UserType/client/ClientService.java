package com.example.Utown.service.UserType.client;

import com.example.Utown.model.UserType.Client;
import java.util.Optional;


public interface ClientService {
        Optional<Client> findByUsername(String username);
}


