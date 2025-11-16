package com.service.packages.controller;

import com.service.packages.dto.request.ProductRequest;
import com.service.packages.dto.request.ProductUpdateRequest;
import com.service.packages.dto.request.SoftDeleteRequest;
import com.service.packages.dto.response.ApiResponse;
import com.service.packages.dto.response.ProductResponse;
import com.service.packages.service.ProductService;
import com.service.packages.util.ImageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Product Controller", description = "APIs for managing products")
public class ProductController {

    private final ProductService productService;

    @PostMapping(value = "/add-product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Add a new product with image upload",
            description = "Creates a new product with image file upload (multipart/form-data)")
    public ResponseEntity<ApiResponse> addProductWithImage(
            @Validated @ModelAttribute ProductRequest productRequest) {
        log.info("Received request to add product with image: {}", productRequest.getProdCode());
        try {
            // Convert image file to Base64 if provided
            if (productRequest.getProdImageFile() != null && !productRequest.getProdImageFile().isEmpty()) {
                ImageUtil.validateImageFile(productRequest.getProdImageFile());
                String base64Image = ImageUtil.convertToBase64(productRequest.getProdImageFile());
                productRequest.setProdImage(base64Image);
            }

            ProductResponse response = productService.addProduct(productRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(HttpStatus.CREATED, "Product added successfully with image", response));
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage()));
        } catch (IOException e) {
            log.error("Error processing image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Error processing image: " + e.getMessage()));
        } catch (RuntimeException e) {
            log.error("Error adding product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @PostMapping(value = "/update-product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update an existing product with image upload",
            description = "Updates product information with image file upload (multipart/form-data)")
    public ResponseEntity<ApiResponse> updateProductWithImage(
            @Validated @ModelAttribute ProductUpdateRequest productUpdateRequest) {
        log.info("Received request to update product with image: {}", productUpdateRequest.getProdId());
        try {
            // Convert image file to Base64 if provided
            if (productUpdateRequest.getProdImageFile() != null && !productUpdateRequest.getProdImageFile().isEmpty()) {
                ImageUtil.validateImageFile(productUpdateRequest.getProdImageFile());
                String base64Image = ImageUtil.convertToBase64(productUpdateRequest.getProdImageFile());
                productUpdateRequest.setProdImage(base64Image);
            }

            ProductResponse response = productService.updateProduct(productUpdateRequest);
            return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "Product updated successfully with image", response));
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage()));
        } catch (IOException e) {
            log.error("Error processing image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Error processing image: " + e.getMessage()));
        } catch (RuntimeException e) {
            log.error("Error updating product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @PostMapping("/soft-delete")
    @Operation(summary = "Soft delete a product",
            description = "Sets product status to INACTIVE (soft delete)")
    public ResponseEntity<ApiResponse> softDeleteProduct(
            @Validated @RequestBody SoftDeleteRequest request) {
        log.info("Received request to soft delete product: {}", request.getProdId());
        try {
            ProductResponse response = productService.softDeleteProduct(request.getProdId());
            return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "Product soft deleted successfully", response));
        } catch (RuntimeException e) {
            log.error("Error soft deleting product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Search products by product name and/or product code")
    public ResponseEntity<ApiResponse> searchProducts(
            @Parameter(description = "Product name to search (partial match)")
            @RequestParam(required = false) String productName,
            @Parameter(description = "Product code to search (partial match)")
            @RequestParam(required = false) String productCode) {
        log.info("Received search request - name: {}, code: {}", productName, productCode);

        if ((productName == null || productName.trim().isEmpty()) &&
                (productCode == null || productCode.trim().isEmpty())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST,
                            "At least one search parameter (productName or productCode) is required"));
        }

        List<ProductResponse> products = productService.searchProducts(productName, productCode);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "Products retrieved successfully", products));
    }

    @GetMapping("/report")
    @Operation(summary = "Download product report as PDF",
            description = "Generates and downloads a PDF report of products within the specified date range")
    public ResponseEntity<byte[]> downloadProductReport(
            @Parameter(description = "Start date (format: yyyy-MM-ddTHH:mm:ss)")
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (format: yyyy-MM-ddTHH:mm:ss)")
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("Received request to generate product report from {} to {}", startDate, endDate);

        try {
            byte[] pdfBytes = productService.generateProductReport(startDate, endDate);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment",
                    "product-report-" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (RuntimeException e) {
            log.error("Error generating report: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}