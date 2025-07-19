package com.example.Utown.repository.UserType;


import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.clientDTO.ClientProfileUpdateDto;
import com.example.Utown.dto.clientDTO.ClientDetailsDto;
import com.example.Utown.dto.clientDTO.ClientInfoDto;
import com.example.Utown.model.UserType.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByUsername(String username);

    @Query("SELECT new com.example.Utown.dto.clientDTO.ClientDetailsDto(" +
            "c.fullName, c.username, a.city, a.fullAddress, SIZE(c.orders)) " +
            "FROM Client c " +
            "LEFT JOIN Address a ON a.id = c.defaultAddress")
    Page<ClientDetailsDto> findAllClientDetails(Pageable pageable);

    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.addresses WHERE c.username = :username")
    Optional<Client> findByUsernameWithAddresses(@Param("username") String username);

    @Query("SELECT new com.example.Utown.dto.clientDTO.ClientInfoDto(" +
            "c.fullName, c.username, a.city, a.fullAddress, SIZE(c.orders), c.fileInfo.id) " +
            "FROM Client c " +
            "LEFT JOIN Address a ON a.id = c.defaultAddress " +
            "WHERE c.id = :id")
    Optional<ClientInfoDto> findClientInfoById(@Param("id") Long id);

    @Query("SELECT new com.example.Utown.dto.addressDTO.AddressDto(" +
            "a.id, a.area, a.city, a.details, a.fullAddress, a.latitude, a.longitude, " +
            "a.postCode, a.state, a.street, a.intercomCode, a.typeAddress) " +
            "FROM Client c JOIN c.addresses a WHERE c.username = :username")
    List<AddressDto> getAddressesByClient(@Param("username") String username);

}

