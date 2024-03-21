package com.dev.server.repository;

import com.dev.server.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT p FROM Product p WHERE p.price >= :priceMin AND p.price <= :priceMax" +
            " AND (:query is null or lower(p.name) like lower(concat('%', :query, '%')) )" +
            " AND (COALESCE(:categoryIds, null) IS null or p.category.id IN (:categoryIds))")
    List<Product> find(@Param("priceMin")Double priceMin, @Param("priceMax")Double priceMax,
                       @Param("query")String query,@Param("categoryIds") List<Long> categoryIds,
                       Pageable pageable);
}
