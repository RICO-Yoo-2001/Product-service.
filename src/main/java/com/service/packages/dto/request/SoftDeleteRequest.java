package com.service.packages.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Soft delete request")
public class SoftDeleteRequest {

    @NotNull(message = "Product ID is required")
    @Schema(description = "Product ID to soft delete", example = "1", required = true)
    private Long prodId;
}