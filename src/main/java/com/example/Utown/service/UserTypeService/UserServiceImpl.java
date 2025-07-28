package com.example.Utown.service.UserTypeService;

import com.example.Utown.exception.UserNotFoundException;
import com.example.Utown.model.User;
import com.example.Utown.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override //For Client
    public boolean changePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }



}
