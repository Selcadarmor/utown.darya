package com.example.Utown.repository.UserType;

import com.example.Utown.dto.clientDTO.ClientProfileUpdateDto;
import com.example.Utown.model.UserType.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByUsername(String username);

    @Query("SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.addresses LEFT JOIN FETCH c.orders")
    List<Client> findAllWithAddressesAndOrders();



    @Query("""
        SELECT c FROM Client c LEFT JOIN FETCH c.addresses LEFT JOIN FETCH c.orders LEFT JOIN FETCH c.fileInfo WHERE c.id = :clientId""")
    Optional<Client> findAllClientInfoById(@Param("clientId")Long clientId);


    Optional<ClientProfileUpdateDto> findUserProfileById(Long id);

}

