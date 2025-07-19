package com.example.Utown.repository;

import com.example.Utown.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    @Query("SELECT o FROM Option o LEFT JOIN FETCH o.elements WHERE o.id IN :ids")
    List<Option> findAllWithElementsByIds(@Param("ids") List<Long> ids);


}
