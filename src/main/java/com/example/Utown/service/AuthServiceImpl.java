package com.example.Utown.service;

import com.example.Utown.config.Utills.JWTUtils;
import com.example.Utown.dto.tokens.JWTRequest;
import com.example.Utown.dto.tokens.JWTResponse;
import com.example.Utown.dto.clientDto.ClientRegistrationDto;
import com.example.Utown.exception.UserAlreadyExistsException;
import com.example.Utown.model.enumFiles.Roles;
import com.example.Utown.service.UserType.client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final ClientService clientService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public JWTResponse createAuthToken(JWTRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        refreshTokenService.createRefreshToken(user.getUsername(), refreshToken);

        return new JWTResponse(accessToken, refreshToken);
    }

    @Override
    public void registerClient(ClientRegistrationDto dto) {
        if (clientService.findByUsername(dto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(dto.getUsername());
        }
        clientService.saveClient(dto, Roles.ROLE_CLIENT);
    }
}




