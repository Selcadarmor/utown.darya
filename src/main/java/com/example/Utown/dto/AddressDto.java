package com.example.Utown.dto;

import com.example.Utown.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long id;
    private String area;
    private String city;
    private String details;
    private String fullAddress;
    private String postalCode;
    private Float latitude;
    private Float longitude;
    private String state;
    private String street;
    private String intercomCode;
    private Integer typeAddress;
    private Restaurant restaurant;
    private Set<User> users;
}
