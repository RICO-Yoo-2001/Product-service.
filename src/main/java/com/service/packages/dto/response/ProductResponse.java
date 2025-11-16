package com.service.packages.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long prodId;
    private String prodName;
    private String prodCode;
    private String prodDescription;
    private String prodImage; // Base64 encoded
    private BigDecimal prodCost;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
