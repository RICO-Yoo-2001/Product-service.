package com.service.packages.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Prod_id")
    private Long prodId;

    @Column(name = "Prod_name", nullable = false, length = 255)
    private String prodName;

    @Column(name = "prod_code", unique = true, nullable = false, length = 50)
    private String prodCode;

    @Column(name = "prod_description", columnDefinition = "TEXT")
    private String prodDescription;

    @Column(name = "prod_image", columnDefinition = "LONGTEXT")
    private String prodImage; // Base64 encoded

    @Column(name = "prod_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal prodCost;

    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE or INACTIVE

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "ACTIVE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
