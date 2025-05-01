package com.example.product.controller;

import com.example.product.dto.Product.ProductCreateRequest;
import com.example.product.dto.Product.ProductDTO;
import com.example.product.dto.Product.ProductListDTO;
import com.example.product.dto.Product.ProductSearchResponse;
import com.example.product.dto.Product.ProductUpdateRequest;
import com.example.product.enums.ProductStatus;
import com.example.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Создать новый продукт
     */
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductCreateRequest request) throws IOException {
        ProductDTO createdProduct = productService.createProduct(request);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    /**
     * Обновить существующий продукт
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request) throws IOException {
        ProductDTO updatedProduct = productService.updateProduct(id, request);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Получить продукт по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Получить продукт по slug
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ProductDTO> getProductBySlug(@PathVariable String slug) {
        return productService.getProductBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Удалить продукт
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получить все продукты
     */
    @GetMapping
    public ResponseEntity<List<ProductListDTO>> getAllProducts() {
        List<ProductListDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Получить продукты с пагинацией
     */
    @GetMapping("/page")
    public ResponseEntity<Page<ProductListDTO>> getProductsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction dir = direction.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        Page<ProductListDTO> products = productService.getProductsPaginated(pageable);

        return ResponseEntity.ok(products);
    }

    /**
     * Получить продукты по бренду
     */
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<ProductListDTO>> getProductsByBrand(@PathVariable Long brandId) {
        List<ProductListDTO> products = productService.getProductsByBrand(brandId);
        return ResponseEntity.ok(products);
    }

    /**
     * Получить продукты по категории
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductListDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductListDTO> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    /**
     * Получить продукты по статусу
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProductListDTO>> getProductsByStatus(
            @PathVariable ProductStatus status) {
        List<ProductListDTO> products = productService.getProductsByStatus(status);
        return ResponseEntity.ok(products);
    }

    /**
     * Получить избранные продукты
     */
    @GetMapping("/featured")
    public ResponseEntity<List<ProductListDTO>> getFeaturedProducts() {
        List<ProductListDTO> products = productService.getFeaturedProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Поиск продуктов
     */
    @GetMapping("/search")
    public ResponseEntity<ProductSearchResponse> searchProducts(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        ProductSearchResponse response = productService.searchProducts(query, pageable);

        return ResponseEntity.ok(response);
    }

    /**
     * Поиск продуктов по цене
     */
    @GetMapping("/search/price")
    public ResponseEntity<ProductSearchResponse> searchProductsByPrice(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        ProductSearchResponse response = productService.searchProductsByPrice(minPrice, maxPrice, pageable);

        return ResponseEntity.ok(response);
    }

    /**
     * Загрузить изображение для продукта
     */
    @PostMapping("/{id}/images")
    public ResponseEntity<ProductDTO> addProductImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile image,
            @RequestParam(defaultValue = "false") boolean main) throws IOException {

        ProductDTO updatedProduct = productService.addProductImage(id, image, main);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Обновить категории продукта
     */
    @PutMapping("/{id}/categories")
    public ResponseEntity<ProductDTO> updateProductCategories(
            @PathVariable Long id,
            @RequestBody List<Long> categoryIds) {

        ProductDTO updatedProduct = productService.updateProductCategories(id, categoryIds);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Увеличить счетчик просмотров продукта
     */
    @PostMapping("/{id}/view")
    public ResponseEntity<Void> incrementViewCount(@PathVariable Long id) {
        productService.incrementViewCount(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Обновить количество товара на складе
     */
    @PutMapping("/{id}/stock")
    public ResponseEntity<Void> updateStockQuantity(
            @PathVariable Long id,
            @RequestParam Integer quantity) {

        productService.updateStockQuantity(id, quantity);
        return ResponseEntity.ok().build();
    }

    /**
     * Получить общее количество продуктов
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getProductCount() {
        long count = productService.getProductCount();
        return ResponseEntity.ok(count);
    }

    /**
     * Получить количество продуктов по статусу
     */
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> getProductCountByStatus(@PathVariable ProductStatus status) {
        long count = productService.getProductCountByStatus(status);
        return ResponseEntity.ok(count);
    }
}