package com.service.packages.service;

import com.service.packages.dto.request.ProductRequest;
import com.service.packages.dto.request.ProductUpdateRequest;
import com.service.packages.dto.response.ProductResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductService {

    ProductResponse addProduct(ProductRequest productRequest);

    ProductResponse updateProduct(ProductUpdateRequest productUpdateRequest);

    List<ProductResponse> searchProducts(String productName, String productCode);

    ProductResponse softDeleteProduct(Long prodId);

    byte[] generateProductReport(LocalDateTime startDate, LocalDateTime endDate);
}
