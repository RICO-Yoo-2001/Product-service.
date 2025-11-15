package com.service.packages.service.impl;

import com.service.packages.dto.request.ProductRequest;
import com.service.packages.dto.request.ProductUpdateRequest;
import com.service.packages.dto.response.ProductResponse;
import com.service.packages.entity.Product;
import com.service.packages.repository.ProductRepository;
import com.service.packages.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse addProduct(ProductRequest productRequest) {
        log.info("Adding new product with code: {}", productRequest.getProdCode());

        // Check if product code already exists
        if (productRepository.existsByProdCode(productRequest.getProdCode())) {
            log.warn("Product with code {} already exists", productRequest.getProdCode());
            throw new RuntimeException("Product with code " + productRequest.getProdCode() + " already exists");
        }

        Product product = Product.builder()
                .prodName(productRequest.getProdName())
                .prodCode(productRequest.getProdCode())
                .prodDescription(productRequest.getProdDescription())
                .prodImage(productRequest.getProdImage())
                .prodCost(productRequest.getProdCost())
                .build();

        Product savedProduct = productRepository.save(product);
        log.info("Product added successfully with ID: {}", savedProduct.getProdId());

        return mapToResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(ProductUpdateRequest productUpdateRequest) {
        log.info("Updating product with ID: {}", productUpdateRequest.getProdId());

        // Find the product by ID - prodId cannot be changed, it's only used to identify the product
        Product product = productRepository.findById(productUpdateRequest.getProdId())
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", productUpdateRequest.getProdId());
                    return new RuntimeException("Product not found with ID: " + productUpdateRequest.getProdId());
                });

        // Verify the product ID matches (safeguard - prodId cannot be changed)
        Long originalProdId = product.getProdId();
        log.debug("Updating product with original ID: {} (ID cannot be changed)", originalProdId);

        // Check if product code is being changed and if new code already exists
        if (!product.getProdCode().equals(productUpdateRequest.getProdCode())) {
            if (productRepository.existsByProdCode(productUpdateRequest.getProdCode())) {
                log.warn("Product with code {} already exists", productUpdateRequest.getProdCode());
                throw new RuntimeException("Product with code " + productUpdateRequest.getProdCode() + " already exists");
            }
        }

        // Update only the allowed fields - prodId is NOT updated (it remains unchanged)
        product.setProdName(productUpdateRequest.getProdName());
        product.setProdCode(productUpdateRequest.getProdCode());
        product.setProdDescription(productUpdateRequest.getProdDescription());
        product.setProdImage(productUpdateRequest.getProdImage());
        product.setProdCost(productUpdateRequest.getProdCost());
        // Note: product.setProdId() is intentionally NOT called - prodId cannot be changed

        Product updatedProduct = productRepository.save(product);
        
        // Verify the ID remains unchanged after update
        if (!updatedProduct.getProdId().equals(originalProdId)) {
            log.error("CRITICAL: Product ID was changed from {} to {}", originalProdId, updatedProduct.getProdId());
            throw new RuntimeException("Product ID cannot be changed");
        }
        
        log.info("Product updated successfully with ID: {} (ID unchanged)", updatedProduct.getProdId());

        return mapToResponse(updatedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(String productName, String productCode) {
        log.info("Searching products with name: {}, code: {}", productName, productCode);

        List<Product> products = productRepository.searchProducts(productName, productCode);
        log.info("Found {} products matching search criteria", products.size());

        return products.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .prodId(product.getProdId())
                .prodName(product.getProdName())
                .prodCode(product.getProdCode())
                .prodDescription(product.getProdDescription())
                .prodImage(product.getProdImage())
                .prodCost(product.getProdCost())
                .build();
    }
}
