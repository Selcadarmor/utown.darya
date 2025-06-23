package com.example.Utown.controller;

import com.example.Utown.dto.ClientCreateDto;
import com.example.Utown.dto.ClientDto;
import com.example.Utown.dto.ClientUpdateDto;
import com.example.Utown.service.UserType.admin.AdminServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ResponseStatus
@RequestMapping("/admin/clients")
@RequiredArgsConstructor
@Tag(name = "Admin-Client Management", description = "Manage clients via the admin panel")
public class AdminClientController {

    private final AdminServiceImpl adminService;

    @Operation(summary = "Get all clients", description = "Returns a list of all registered clients.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of clients retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        return ResponseEntity.ok(adminService.getAllClients());
    }

    @Operation(summary = "Get client by id", description = "Returns a client with the given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getClientById(id));
    }

    @Operation(summary = "Create a new client", description = "Registers a new client in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client create successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<ClientDto> clientCreateDtoResponseEntity(@Valid @RequestBody ClientCreateDto clientCreateDto) {
        ClientDto created = adminService.createClient(clientCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update client by Id", description = "Updates information of an existing client.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Client nit found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> clientUpdateDtoResponseEntity(@PathVariable Long id, @Valid @RequestBody ClientUpdateDto clientUpdateDto) {
        return ResponseEntity.ok(adminService.updateClient(id, clientUpdateDto));
    }

    @Operation(summary = "Delete client by Id", description = "Removes a client from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        adminService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

}
