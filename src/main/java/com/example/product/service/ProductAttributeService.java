package com.example.product.service;


import com.example.product.dto.ProductAttribute.ProductAttributeCreateDTO;
import com.example.product.dto.ProductAttribute.ProductAttributeDTO;
import com.example.product.dto.ProductAttribute.ProductAttributeListDTO;
import com.example.product.dto.ProductAttribute.ProductAttributeUpdateDTO;

import java.util.List;

public interface ProductAttributeService {

    /**
     * Получить список всех атрибутов для продукта
     */
    ProductAttributeListDTO getProductAttributes(Long productId);

    /**
     * Получить конкретный атрибут по ID
     */
    ProductAttributeDTO getAttributeById(Long attributeId);

    /**
     * Создать новый атрибут для продукта
     */
    ProductAttributeDTO createAttribute(ProductAttributeCreateDTO createDTO);

    /**
     * Обновить существующий атрибут
     */
    ProductAttributeDTO updateAttribute(Long attributeId, ProductAttributeUpdateDTO updateDTO);

    /**
     * Удалить атрибут
     */
    void deleteAttribute(Long attributeId);

    /**
     * Создать несколько атрибутов для продукта
     */
    List<ProductAttributeDTO> createAttributes(Long productId, List<ProductAttributeCreateDTO> createDTOs);

    /**
     * Удалить все атрибуты продукта
     */
    void deleteAllProductAttributes(Long productId);

    /**
     * Найти атрибуты с определенным цветом
     */
    List<ProductAttributeDTO> findAttributesByColor(String color);

    /**
     * Найти атрибуты с определенным размером
     */
    List<ProductAttributeDTO> findAttributesBySize(String size);

    /**
     * Найти атрибуты с определенным материалом
     */
    List<ProductAttributeDTO> findAttributesByMaterial(String material);
}