package com.example.Utown.dto.adminDto;


import com.example.Utown.dto.otherDto.RoleDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreateRequestDto {
    @NotNull(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,15}$", message = "Invalid username")
    private String username;

    @NotNull(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters long")
    private String password;

    private boolean platform;

    @NotNull(message = "Default address is required")
    private Long defaultAddress;
    @NotNull(message = "Restaurant id is required")
    private Long restaurantId;

    @NotNull(message = "Role is required")
    private Set<RoleDto> roles;
}
