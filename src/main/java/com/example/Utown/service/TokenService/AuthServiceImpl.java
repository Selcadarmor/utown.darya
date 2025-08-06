package com.example.Utown.service.TokenService;

import com.example.Utown.config.Utills.JWTUtils;
import com.example.Utown.dto.AdminDTO.AdminRegistrationDto;
import com.example.Utown.dto.tokens.JWTRequest;
import com.example.Utown.dto.tokens.JWTResponse;
import com.example.Utown.dto.clientDTO.ClientRegistrationDto;
import com.example.Utown.exception.UserAlreadyExistsException;
import com.example.Utown.model.enumFiles.Roles;
import com.example.Utown.repository.UserRepository;
import com.example.Utown.repository.UserType.ClientRepository;
import com.example.Utown.service.UserTypeService.ClientService;
import com.example.Utown.service.UserTypeService.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final ClientRepository clientRepository;
    private final ClientService clientService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public JWTResponse createAuthToken(JWTRequest authRequest) {
        log.info("Attempting authentication for username: {}", authRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();

        log.info("Authentication successful for username: {}", user.getUsername());

        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        refreshTokenService.createRefreshToken(user.getUsername(), refreshToken);

        return new JWTResponse(accessToken, refreshToken);
    }


    @Override
    public void registration(ClientRegistrationDto dto) {
        log.info("Attempting registration for client username: {}", dto.getUsername());

        if (clientRepository.findByUsername(dto.getUsername()).isPresent()) {
            log.warn("Registration failed: username '{}' already exists", dto.getUsername());
            throw new UserAlreadyExistsException(dto.getUsername());
        }

        clientService.save(dto, Roles.ROLE_CLIENT);
        log.info("Client registration successful for username: {}", dto.getUsername());
    }

    @Override
    public void adminRegistration(AdminRegistrationDto dto) {
        log.info("Attempting registration for admin username: {}", dto.getUsername());

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            log.warn("Admin registration failed: username '{}' already exists", dto.getUsername());
            throw new UserAlreadyExistsException(dto.getUsername());
        }

        userService.createAdmin(dto, Roles.ROLE_ADMIN);
        log.info("Admin registration successful for username: {}", dto.getUsername());
    }

}




