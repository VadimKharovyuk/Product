package com.example.product.service;

import com.example.product.dto.Product.ProductCreateRequest;
import com.example.product.dto.Product.ProductDTO;
import com.example.product.dto.Product.ProductListDTO;
import com.example.product.dto.Product.ProductSearchResponse;
import com.example.product.dto.Product.ProductUpdateRequest;
import com.example.product.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductDTO createProduct(ProductCreateRequest request) throws IOException;

    ProductDTO updateProduct(Long id, ProductUpdateRequest request) throws IOException;


    Optional<ProductDTO> getProductById(Long id);


    Optional<ProductDTO> getProductBySlug(String slug);

    void deleteProduct(Long id);

    List<ProductListDTO> getAllProducts();


    Page<ProductListDTO> getProductsPaginated(Pageable pageable);


    List<ProductListDTO> getProductsByBrand(Long brandId);

    List<ProductListDTO> getProductsByCategory(Long categoryId);


    List<ProductListDTO> getProductsByStatus(ProductStatus status);


    List<ProductListDTO> getFeaturedProducts();

    ProductSearchResponse searchProducts(String query, Pageable pageable);

    ProductSearchResponse searchProductsByPrice(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);


    ProductDTO addProductImage(Long productId, MultipartFile image, boolean isMain) throws IOException;

    ProductDTO updateProductCategories(Long productId, List<Long> categoryIds);

    void incrementViewCount(Long productId);


    void updateStockQuantity(Long productId, Integer quantity);


    long getProductCount();


    long getProductCountByStatus(ProductStatus status);
}