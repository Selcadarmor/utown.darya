package com.example.Utown.repository.UserType;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.clientDTO.ClientDetailsDto;
import com.example.Utown.dto.clientDTO.ClientInfoDto;
import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
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
            "LEFT JOIN Address a ON a.id = c.defaultAddress " +
            "WHERE " +
            "(:query IS NULL OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :query, '%'))  " +
            "OR LOWER(c.username) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "(:isActive IS NULL OR c.isActive = :isActive)")
    Page<ClientDetailsDto> findAllClientDetails( @Param("query") String query,
                                                 @Param("isActive") Boolean isActive,
                                                 Pageable pageable);

    @Query("SELECT new com.example.Utown.dto.clientDTO.ClientInfoDto(" +
            "c.fullName, c.username, a.city, a.fullAddress, SIZE(c.orders), c.fileInfo.id, c.fileInfo.path) " +
            "FROM Client c " +
            "LEFT JOIN Address a ON a.id = c.defaultAddress " +
            "WHERE c.id = :id")
    Optional<ClientInfoDto> findClientInfoById(@Param("id") Long id);

    @Query("SELECT new com.example.Utown.dto.addressDTO.AddressDto(" +
            "null, null, a.street, a.city, a.details, a.intercomCode, c.id) " +
            "FROM Client c JOIN c.addresses a " +
            "WHERE c.username = :username")
    List<AddressDto> getAddressesByClient(@Param("username") String username);

    @Query("SELECT DISTINCT new com.example.Utown.dto.restaurantDTO.RestaurantForClientDto(" +
            "r.id, r.title, f.path, r.description, d.price, r.deliveryTime, " +
            "r.isRecommended, r.isActive, d.isDeleted, " +
            "r.rating, r.totalRatings) " +
            "FROM Client c " +
            "JOIN c.favoriteRestaurants r " +
            "LEFT JOIN r.fileInfo f " +
            "LEFT JOIN r.deliveries d " +
            "WHERE c.username = :username")
    List<RestaurantForClientDto> findFavoriteRestaurants(String username);
}

