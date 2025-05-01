package com.example.product.repository;

import com.example.product.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    /**
     * Получить все изображения для продукта
     */
    List<ProductImage> findByProductId(Long productId);

    /**
     * Получить все изображения для продукта с сортировкой
     */
    List<ProductImage> findByProductIdOrderBySortOrderAsc(Long productId);

    /**
     * Получить максимальный порядок сортировки для продукта
     */
    @Query("SELECT MAX(pi.sortOrder) FROM ProductImage pi WHERE pi.product.id = :productId")
    Integer findMaxSortOrderByProductId(@Param("productId") Long productId);

    /**
     * Обновить порядок сортировки изображения
     */
    @Modifying
    @Query("UPDATE ProductImage pi SET pi.sortOrder = :sortOrder WHERE pi.id = :imageId")
    void updateSortOrder(@Param("imageId") Long imageId, @Param("sortOrder") Integer sortOrder);

    /**
     * Сбросить главное изображение для всех изображений продукта
     */
    @Modifying
    @Query("UPDATE ProductImage pi SET pi.imageType = com.example.product.enums.ImageType.GALLERY " +
            "WHERE pi.product.id = :productId AND pi.imageType = com.example.product.enums.ImageType.MAIN")
    void resetMainImageByProductId(@Param("productId") Long productId);

    /**
     * Удалить все изображения для продукта
     */
    void deleteByProductId(Long productId);
}