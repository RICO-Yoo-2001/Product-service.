package com.service.packages.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
}
