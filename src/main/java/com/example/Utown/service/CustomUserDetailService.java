package com.example.Utown.service;

import com.example.Utown.model.User;
import com.example.Utown.repository.UserType.AdminRepository;
import com.example.Utown.repository.UserType.ClientRepository;
import com.example.Utown.repository.UserType.RestaurantAdminRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final ClientRepository clientRepository;
    private final AdminRepository adminRepository;
    private final RestaurantAdminRepository restaurantAdminRepository;

    @Autowired
    public CustomUserDetailService(ClientRepository clientRepository,
                                   AdminRepository adminRepository,
                                   RestaurantAdminRepository restaurantAdminRepository) {
        this.clientRepository = clientRepository;
        this.adminRepository = adminRepository;
        this.restaurantAdminRepository = restaurantAdminRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = clientRepository.findByUsername(username)
                .map(u -> (User) u)
                .or(() -> adminRepository.findByUsername(username).map(u -> (User) u))
                .or(() -> restaurantAdminRepository.findByUsername(username).map(u -> (User) u))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return user;
    }
}

