package com.example.Utown.controller;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.mapper.AddressMapper;
import com.example.Utown.model.Address;
import com.example.Utown.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    @PostMapping("/create")
    public ResponseEntity<AddressDto> createAddress(@RequestBody AddressDto dto) {
        Address address = addressService.createAddress(dto);
        AddressDto responseDto = addressMapper.addressToDto(address);
        return ResponseEntity.status(201).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable Long id) {
        AddressDto dto = addressService.getAddressById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        List<AddressDto> addresses = addressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long id, @RequestBody AddressDto dto) {
        Address updatedAddress = addressService.updateAddress(id, dto);
        AddressDto responseDto = addressMapper.addressToDto(updatedAddress);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

}

