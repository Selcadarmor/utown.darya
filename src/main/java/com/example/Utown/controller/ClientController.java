package com.example.Utown.controller;

import com.example.Utown.dto.userDto.UserChangePasswordDto;
import com.example.Utown.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
@Tag(name = "Client", description = "Client specific operations")
public class ClientController {

    private final UserService userService;


    @PutMapping("/change-password")
    @Operation(summary = "Change client password", description = "")
    public ResponseEntity<String> changePassword(
            @RequestBody @Valid UserChangePasswordDto dto,
            @AuthenticationPrincipal User user
    ) {
        userService.changePassword(user.getUsername(), dto);
        return ResponseEntity.ok("Password changed successfully");
    }
}

