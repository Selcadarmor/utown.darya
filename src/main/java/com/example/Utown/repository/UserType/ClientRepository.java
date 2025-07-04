package com.example.Utown.repository.UserType;

import com.example.Utown.dto.adminDto.ClientInfoDto;
import com.example.Utown.dto.clientDto.ClientProfileUpdateDto;
import com.example.Utown.model.UserType.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByUsername(String username);

    @Query("""
        SELECT new com.example.Utown.dto.adminDto.ClientInfoDto(
            c.id,
            c.fullName,
            c.username,
            a.city,
            a.fullAddress,
            COUNT(o)
        
        )
        FROM Client c
        LEFT JOIN Address  a ON a.id = c.defaultAddress
        LEFT JOIN c.orders o
        GROUP BY c.id, c.fullName,c.username, a.city, a.fullAddress
        """)
    List<ClientInfoDto> findAllClientInfos();

    @Query("""
        SELECT new com.example.Utown.dto.adminDto.ClientInfoDto(
            c.id,
            c.fullName,
            c.username,
            a.fullAddress,
            COUNT(o),
            c.fileInfo.id
        )
        FROM Client c
        LEFT JOIN Address a ON a.id = c.defaultAddress
        LEFT JOIN c.orders o
        LEFT JOIN c.fileInfo.id
        WHERE c.id =:id
        GROUP BY c.id, c.fullName,c.username, a.fullAddress, c.fileInfo.id
        """)
    Optional<ClientInfoDto> findAllClientInfoById(Long id);

    @Query("""
    SELECT new com.example.Utown.dto.clientDto.ClientProfileUpdateDto(
           c.fullName,
           c.username,
           a.fullAddress
       )
       FROM Client c
       LEFT JOIN Address a ON a.id = c.defaultAddress
       WHERE c.id = :id
""")
    Optional<ClientProfileUpdateDto> findUserProfileById(Long id);

}

