package com.service.packages.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Product creation request")
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    @Schema(description = "Product name", example = "Laptop", required = true)
    private String prodName;

    @NotBlank(message = "Product code is required")
    @Size(max = 50, message = "Product code must not exceed 50 characters")
    @Schema(description = "Product code", example = "PROD001", required = true)
    private String prodCode;

    @Schema(description = "Product description", example = "High-performance laptop")
    private String prodDescription;

    @Schema(description = "Product image file (JPEG, PNG, GIF, WEBP - max 5MB)", type = "string", format = "binary")
    private MultipartFile prodImageFile;

    @Schema(hidden = true)
    private String prodImage; // Base64 encoded - set internally

    @NotNull(message = "Product cost is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Product cost must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Product cost must have at most 8 integer digits and 2 decimal places")
    @Schema(description = "Product cost", example = "999.99", required = true)
    private BigDecimal prodCost;
}