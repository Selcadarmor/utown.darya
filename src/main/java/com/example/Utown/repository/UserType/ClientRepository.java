package com.example.Utown.repository.UserType;

import com.example.Utown.dto.clientDTO.ClientProfileUpdateDto;
import com.example.Utown.model.UserType.Client;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByUsername(String username);

    @EntityGraph(attributePaths = {"addresses", "orders"})
    @Query("SELECT c FROM Client c")
    List<Client> findAllWithAddressesAndOrders();


    @EntityGraph(attributePaths = {"addresses", "orders", "fileInfo"})
    @Query("SELECT c FROM Client c WHERE c.id = :id")
    Optional<Client> findAllClientInfoById(@Param("id")Long id);


    Optional<ClientProfileUpdateDto> findUserProfileById(Long id);

}

