package com.service.packages.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateRequest {

    @NotNull(message = "Product ID is required")
    private Long prodId;

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    private String prodName;

    @NotBlank(message = "Product code is required")
    @Size(max = 50, message = "Product code must not exceed 50 characters")
    private String prodCode;

    private String prodDescription;

    private String prodImage; // Base64 encoded

    @NotNull(message = "Product cost is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Product cost must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Product cost must have at most 8 integer digits and 2 decimal places")
    private BigDecimal prodCost;
}
