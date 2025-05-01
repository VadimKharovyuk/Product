package com.example.product.repository;

import com.example.product.model.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {

    /**
     * Найти все атрибуты для определенного продукта
     */
    List<ProductAttribute> findByProductId(Long productId);

    /**
     * Удалить все атрибуты для определенного продукта и вернуть количество удаленных записей
     */
    @Modifying
    @Query("DELETE FROM ProductAttribute pa WHERE pa.product.id = :productId")
    int deleteByProductId(@Param("productId") Long productId);

    /**
     * Найти атрибуты по цвету (без учета регистра)
     * Используем точное имя поля из сущности - Color с большой буквы
     */
    List<ProductAttribute> findByColorIgnoreCase(String color);

    /**
     * Найти атрибуты по размеру (без учета регистра)
     * Используем точное имя поля из сущности - Size с большой буквы
     */
    List<ProductAttribute> findBySizeIgnoreCase(String size);

    /**
     * Найти атрибуты по материалу (без учета регистра)
     * Используем точное имя поля из сущности - Material с большой буквы
     */
    List<ProductAttribute> findByMaterialIgnoreCase(String material);

    /**
     * Найти атрибуты по цвету, размеру и материалу
     * Используем точные имена полей из сущности с большой буквы
     */
    List<ProductAttribute> findByColorIgnoreCaseAndSizeIgnoreCaseAndMaterialIgnoreCase(
            String color, String size, String material);

    /**
     * Проверить существование атрибута с такими параметрами для продукта
     * Используем точные имена полей из сущности с большой буквы
     */
    boolean existsByProductIdAndColorIgnoreCaseAndSizeIgnoreCaseAndMaterialIgnoreCase(
            Long productId, String color, String size, String material);

    /**
     * Подсчитать количество атрибутов с определенным цветом
     * Используем точное имя поля из сущности - Color с большой буквы
     */
    long countByColorIgnoreCase(String color);

    /**
     * Подсчитать количество атрибутов с определенным размером
     * Используем точное имя поля из сущности - Size с большой буквы
     */
    long countBySizeIgnoreCase(String size);

    /**
     * Подсчитать количество атрибутов с определенным материалом
     * Используем точное имя поля из сущности - Material с большой буквы
     */
    long countByMaterialIgnoreCase(String material);
}