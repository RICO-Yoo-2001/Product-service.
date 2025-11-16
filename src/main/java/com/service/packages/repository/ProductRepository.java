package com.service.packages.repository;

import com.service.packages.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProdCode(String prodCode);

    Optional<Product> findByProdName(String prodName);

    @Query("SELECT p FROM Product p WHERE " +
           "(:productName IS NULL OR LOWER(p.prodName) LIKE LOWER(CONCAT('%', :productName, '%'))) AND " +
           "(:productCode IS NULL OR LOWER(p.prodCode) LIKE LOWER(CONCAT('%', :productCode, '%'))) AND " +
           "p.status = 'ACTIVE'")
    List<Product> searchProducts(@Param("productName") String productName, 
                                  @Param("productCode") String productCode);

    boolean existsByProdCode(String prodCode);

    boolean existsByProdName(String prodName);

    boolean existsByProdCodeAndStatus(String prodCode, String status);

    boolean existsByProdNameAndStatus(String prodName, String status);

    @Query("SELECT p FROM Product p WHERE " +
           "p.status = 'ACTIVE' AND " +
           "(:startDate IS NULL OR p.createdAt >= :startDate) AND " +
           "(:endDate IS NULL OR p.createdAt <= :endDate) " +
           "ORDER BY p.createdAt DESC")
    List<Product> findActiveProductsByDateRange(@Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);
}
