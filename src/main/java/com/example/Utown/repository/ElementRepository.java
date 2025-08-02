package com.example.Utown.repository;

import com.example.Utown.model.Element;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ElementRepository extends JpaRepository<Element, Long> {

    @Query("SELECT e FROM Element e WHERE e.option.id = :optionId AND e.isActive = true AND e.isDeleted = false")
    Set<Element> findByOptionIdAndIsActiveTrueAndIsDeletedFalse(@Param("optionId") Long optionId);

    @Query("SELECT e FROM Element e " +
            "JOIN FETCH e.option o " +
            "JOIN FETCH o.dish d " +
            "JOIN FETCH d.restaurant r " +
            "WHERE e.id = :id ")
    Optional<Element> findWithContext(@Param("id") Long id);
}
