package com.example.Utown.repository;

import com.example.Utown.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findFirstByStreetAndCityAndDetailsAndIntercomCodeAndFullAddressIsNull(
            String street, String city, String details, String intercomCode
    );
}
