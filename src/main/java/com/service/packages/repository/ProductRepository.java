package com.service.packages.repository;

import com.service.packages.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProdCode(String prodCode);

    @Query("SELECT p FROM Product p WHERE " +
           "(:productName IS NULL OR LOWER(p.prodName) LIKE LOWER(CONCAT('%', :productName, '%'))) AND " +
           "(:productCode IS NULL OR LOWER(p.prodCode) LIKE LOWER(CONCAT('%', :productCode, '%')))")
    List<Product> searchProducts(@Param("productName") String productName, 
                                  @Param("productCode") String productCode);

    boolean existsByProdCode(String prodCode);
}
