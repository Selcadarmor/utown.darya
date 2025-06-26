package com.example.Utown.repository.UserType;

import com.example.Utown.dto.adminDto.ClientInfoDto;
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
            addr.city,
            addr.fullAddress,    
            COUNT(o)
        )
        FROM Client c
        LEFT JOIN c.addresses addr
        LEFT JOIN c.orders o
        GROUP BY c.id, c.fullName,c.username, addr.city, addr.fullAddress               
        """)
    List<ClientInfoDto> findAllClientInfos();

    @Query("""
        SELECT new com.example.Utown.dto.adminDto.ClientInfoDto(
            c.id,
            c.fullName,
            c.username,        
            addr.city,
            addr.fullAddress,    
            COUNT(o)
        )
        FROM Client c
        LEFT JOIN c.addresses addr
        LEFT JOIN c.orders o
        GROUP BY c.id, c.fullName,c.username, addr.city, addr.fullAddress               
        """)
    Optional<ClientInfoDto> findAllClientInfoById(Long id);
}

