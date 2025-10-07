package com.example.Utown.repository;

import com.example.Utown.dto.dishDTO.DishDeletedMenuDto;
import com.example.Utown.dto.dishDTO.DishMenuDto;
import com.example.Utown.dto.dishDTO.DishSearchDto;
import com.example.Utown.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    @Query("""
    SELECT d FROM Dish d
    JOIN FETCH d.dishCategory dc
    WHERE (:restaurantId IS NULL OR d.restaurant.id = :restaurantId)
      AND (:title IS NULL OR LOWER(d.title) LIKE LOWER(CONCAT('%', :title, '%')))
      AND (:sort IS NULL OR d.sort = :sort)
      AND (:dishCategoryId IS NULL OR d.dishCategory.id = :dishCategoryId)
      AND (:isActive IS NULL OR d.isActive = :isActive)
""")
    Page<Dish> searchDishesByFilter(
            @Param("restaurantId") Long restaurantId,
            @Param("title") String title,
            @Param("sort") Integer sort,
            @Param("dishCategoryId") Long dishCategoryId,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );
    List<Dish> findAllByRestaurantId(Long restaurantId);

    @EntityGraph(attributePaths = {
            "dishCategory",
            "file",
            "options",
            "options.elements"
    })
    Optional<Dish> findById(Long id);

    @Query("SELECT new com.example.Utown.dto.dishDTO.DishSearchDto(" +
            "d.id, d.title, d.description, d.isActive, d.isDeleted, " +
            "d.price, d.sort, d.restaurant.id, d.dishCategory.id, f.path) " +
            "FROM Dish d " +
            "LEFT JOIN d.file f " +
            "WHERE d.dishCategory.id = :categoryId " +
            "AND d.isActive = true " +
            "AND d.isDeleted = false " +
            "ORDER BY d.sort")
    List<DishSearchDto> findDishDtoByCategoryForClient(@Param("categoryId") Long categoryId);

    @Query("SELECT new com.example.Utown.dto.dishDTO.DishSearchDto(" +
            "d.id, d.title, d.description, d.isActive, d.isDeleted, " +
            "d.price, d.sort, r.id, dc.id, f.path) " +
            "FROM Dish d " +
            "JOIN d.restaurant r " +
            "LEFT JOIN d.dishCategory dc " +
            "LEFT JOIN d.file f " +
            "WHERE d.isDeleted = false " +
            "AND d.isActive = true " +
            "AND r.id = :restaurantId " +
            "AND (LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY d.sort ASC")
    Page<DishSearchDto> searchDishesByRestaurantAndKeyword(
            @Param("restaurantId") Long restaurantId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("SELECT new com.example.Utown.dto.dishDTO.DishMenuDto(" +
            "d.id, d.title, d.description, c.name, d.isActive, d.price, f.id, f.path) " +
            "FROM Dish d " +
            "LEFT JOIN d.dishCategory c " +
            "LEFT JOIN d.file f " +
            "WHERE d.restaurant.id = :restaurantId " +
            "AND c.name = :categoryName " +
            "AND d.isActive = true " +
            "ORDER BY c.name")
    List<DishMenuDto> findActiveDishesByRestaurantIdAndCategoryName(@Param("restaurantId") Long restaurantId,
                                                                    @Param("categoryName") String categoryName);

    @Query("SELECT new com.example.Utown.dto.dishDTO.DishMenuDto(" +
            "d.id, d.title, d.description, c.name, d.isActive, d.price, f.id, f.path) " +
            "FROM Dish d " +
            "LEFT JOIN d.dishCategory c " +
            "LEFT JOIN d.file f " +
            "WHERE d.restaurant.id = :restaurantId " +
            "AND c.name = :categoryName " +
            "AND d.isActive = false " +
            "ORDER BY c.name")
    List<DishMenuDto> findInactiveDishesByRestaurantIdAndCategoryName(@Param("restaurantId") Long restaurantId,
                                                                      @Param("categoryName") String categoryName);

    @Query("SELECT new com.example.Utown.dto.dishDTO.DishDeletedMenuDto(" +
            "d.id, d.title, d.description, c.name, d.price, f.id, f.path, d.isDeleted) " +
            "FROM Dish d " +
            "LEFT JOIN d.dishCategory c " +
            "LEFT JOIN d.file f " +
            "WHERE d.restaurant.id = :restaurantId " +
            "AND d.isDeleted = true " +
            "AND c.name = :categoryName")
    List<DishDeletedMenuDto> findDeletedDishesByRestaurantId(@Param("restaurantId") Long restaurantId,
                                                             @Param("categoryName") String categoryName);

    List<Dish> findByDishCategoryId(Long categoryId);
    boolean existsByDishCategoryId(Long categoryId);

}


