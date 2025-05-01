package com.example.product.repository;

import com.example.product.enums.ProductStatus;
import com.example.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    long countByBrandId(Long brandId);

    /**
     * Найти продукт по URL-slug
     */
    Optional<Product> findBySlug(String slug);

    /**
     * Найти продукты по ID бренда
     */
    List<Product> findByBrandId(Long brandId);

    /**
     * Найти продукты по ID категории
     */
    List<Product> findByCategoriesId(Long categoryId);

    /**
     * Найти продукты по статусу
     */
    List<Product> findByStatus(ProductStatus status);

    /**
     * Найти избранные продукты
     */
    List<Product> findByFeaturedTrue();

    /**
     * Поиск продуктов по названию или описанию
     */
    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String name, String description, Pageable pageable);

    /**
     * Поиск продуктов по диапазону цен
     */
    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    /**
     * Подсчет продуктов по статусу
     */
    long countByStatus(ProductStatus status);



}
