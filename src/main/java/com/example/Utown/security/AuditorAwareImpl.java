package com.example.Utown.security;

import com.example.Utown.model.User;
import com.example.Utown.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
@AllArgsConstructor
@Component
public class AuditorAwareImpl implements AuditorAware<User> {
    private final UserRepository userRepository;
    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
       String username = authentication.getName();
        return userRepository.findByUsername(username);
    }
}
