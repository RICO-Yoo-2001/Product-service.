package com.service.packages.controller;

import com.service.packages.dto.request.ProductRequest;
import com.service.packages.dto.request.ProductUpdateRequest;
import com.service.packages.dto.response.ApiResponse;
import com.service.packages.dto.response.ProductResponse;
import com.service.packages.service.ProductService;
import com.service.packages.util.ImageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
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
            @Parameter(description = "Product name")
            @RequestParam("prodName") String prodName,
            @Parameter(description = "Product code")
            @RequestParam("prodCode") String prodCode,
            @Parameter(description = "Product description")
            @RequestParam(value = "prodDescription", required = false) String prodDescription,
            @Parameter(description = "Product image file (JPEG, PNG, GIF, WEBP - max 5MB)")
            @RequestParam(value = "prodImage", required = false) MultipartFile prodImage,
            @Parameter(description = "Product cost")
            @RequestParam("prodCost") BigDecimal prodCost) {
        log.info("Received request to add product with image: {}", prodCode);
        try {
            // Convert image file to Base64 if provided
            String base64Image = null;
            if (prodImage != null && !prodImage.isEmpty()) {
                ImageUtil.validateImageFile(prodImage);
                base64Image = ImageUtil.convertToBase64(prodImage);
            }

            // Create ProductRequest
            ProductRequest productRequest = ProductRequest.builder()
                    .prodName(prodName)
                    .prodCode(prodCode)
                    .prodDescription(prodDescription)
                    .prodImage(base64Image)
                    .prodCost(prodCost)
                    .build();

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
            @Parameter(description = "Product ID")
            @RequestParam("prodId") Long prodId,
            @Parameter(description = "Product name")
            @RequestParam("prodName") String prodName,
            @Parameter(description = "Product code")
            @RequestParam("prodCode") String prodCode,
            @Parameter(description = "Product description")
            @RequestParam(value = "prodDescription", required = false) String prodDescription,
            @Parameter(description = "Product image file (JPEG, PNG, GIF, WEBP - max 5MB)")
            @RequestParam(value = "prodImage", required = false) MultipartFile prodImage,
            @Parameter(description = "Product cost")
            @RequestParam("prodCost") BigDecimal prodCost) {
        log.info("Received request to update product with image: {}", prodId);
        try {
            // Convert image file to Base64 if provided
            String base64Image = null;
            if (prodImage != null && !prodImage.isEmpty()) {
                ImageUtil.validateImageFile(prodImage);
                base64Image = ImageUtil.convertToBase64(prodImage);
            }

            // Create ProductUpdateRequest
            ProductUpdateRequest productUpdateRequest = ProductUpdateRequest.builder()
                    .prodId(prodId)
                    .prodName(prodName)
                    .prodCode(prodCode)
                    .prodDescription(prodDescription)
                    .prodImage(base64Image)
                    .prodCost(prodCost)
                    .build();

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
}
