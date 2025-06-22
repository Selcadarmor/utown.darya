package com.example.Utown.controller;

import com.example.Utown.dto.СlientDto.ClientChangePasswordDto;
import com.example.Utown.dto.СlientDto.ClientProfileUpdateDto;
import com.example.Utown.service.UserType.ClientService;
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

    private final ClientService clientService;

        @PutMapping("/update")
        @Operation(summary = "Update client profile")
        public ResponseEntity<String> updateProfile(
                @AuthenticationPrincipal User user,
                @RequestBody ClientProfileUpdateDto dto
        ) {
            clientService.updateClientProfile(user.getUsername(), dto);
            return ResponseEntity.ok("Profile updated successfully");
        }

        @PutMapping("/change-password")
        public ResponseEntity<String> changePassword(@RequestBody @Valid ClientChangePasswordDto dto,
                                                 @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
            clientService.changePassword(user.getUsername(), dto);
            return ResponseEntity.ok("Password changed successfully");
    }
}
