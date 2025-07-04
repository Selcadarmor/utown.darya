package com.example.Utown.repository;


import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT new com.example.Utown.dto.addressDTO.AddressDto(" +
            "a.id, a.area, a.city, a.details, a.fullAddress, a.latitude, a.longitude, " +
            "a.postCode, a.state, a.street, a.intercomCode, a.typeAddress) " +
            "FROM Address a WHERE a.id = :id")
    Optional<AddressDto> findAddressById(@Param("id") Long id);

    @Query("SELECT new com.example.Utown.dto.addressDTO.AddressDto(" +
            "a.id, a.area, a.city, a.details, a.fullAddress, a.latitude, a.longitude, " +
            "a.postCode, a.state, a.street, a.intercomCode, a.typeAddress) " +
            "FROM Address a")
    List<AddressDto> findAllAddresses();










    Optional<Address> findByCity(String city);
}
