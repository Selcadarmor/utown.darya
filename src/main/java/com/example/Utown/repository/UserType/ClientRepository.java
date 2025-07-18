package com.example.Utown.repository.UserType;

import com.example.Utown.dto.addressDTO.AddressDto;
import com.example.Utown.dto.restaurantDTO.RestaurantForClientDto;
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

    @Query("SELECT new com.example.Utown.dto.addressDTO.AddressDto(" +
            "a.id, a.area, a.city, a.details, a.fullAddress, a.latitude, a.longitude, " +
            "a.postCode, a.state, a.street, a.intercomCode, a.typeAddress) " +
            "FROM Client c " +
            "JOIN c.addresses a " +
            "WHERE c.username = :username")
    List<AddressDto> getAddressesByClient(@Param("username") String username);

    @Query("SELECT DISTINCT new com.example.Utown.dto.restaurantDTO.RestaurantForClientDto(" +
            "r.id, r.title, f.path, r.description, d.price, r.deliveryTime, r.isRecommended, " +
            "r.isActive, d.isDeleted) " +
            "FROM Client c " +
            "JOIN c.favoriteRestaurants r " +
            "LEFT JOIN r.fileInfo f " +
            "LEFT JOIN r.deliveries d " +
            "WHERE c.username = :username")
    List<RestaurantForClientDto> findFavoriteRestaurants(String username);


}

