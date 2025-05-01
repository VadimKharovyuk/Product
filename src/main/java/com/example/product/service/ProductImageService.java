package com.example.product.service;

import com.example.product.dto.ProductImage.ProductImageCreateDTO;
import com.example.product.dto.ProductImage.ProductImageDTO;
import com.example.product.dto.ProductImage.ProductImageListDTO;
import com.example.product.dto.ProductImage.ProductImageUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductImageService {

    /**
     * Получить список всех изображений для продукта
     */
    ProductImageListDTO getProductImages(Long productId);

    /**
     * Получить конкретное изображение по ID
     */
    ProductImageDTO getImageById(Long imageId);

    /**
     * Добавить новое изображение для продукта
     */
    ProductImageDTO createProductImage(ProductImageCreateDTO createDTO) throws IOException;

    /**
     * Обновить существующее изображение
     */
    ProductImageDTO updateProductImage(Long imageId, ProductImageUpdateDTO updateDTO) throws IOException;

    /**
     * Удалить изображение
     */
    boolean deleteProductImage(Long imageId);

    /**
     * Изменить порядок сортировки изображений
     */
    void updateImageOrder(Long productId, List<Long> imageIds);

    /**
     * Установить главное изображение продукта
     */
    ProductImageDTO setMainProductImage(Long imageId);
}