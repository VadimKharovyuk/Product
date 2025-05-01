package com.example.product.maper;
import com.example.product.dto.ProductImage.ProductImageCreateDTO;
import com.example.product.dto.ProductImage.ProductImageDTO;
import com.example.product.dto.ProductImage.ProductImageListDTO;
import com.example.product.dto.ProductImage.ProductImageUpdateDTO;
import com.example.product.model.Product;
import com.example.product.model.ProductImage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductImageMapper {

    /**
     * Конвертирует entity в DTO
     */
    public ProductImageDTO toDTO(ProductImage image) {
        if (image == null) {
            return null;
        }

        return ProductImageDTO.builder()
                .id(image.getId())
                .productId(image.getProduct() != null ? image.getProduct().getId() : null)
                .imageType(image.getImageType())
                .imageUrl(image.getImageUrl())
                .imageId(image.getImageId())
                .alt(image.getAlt())
                .sortOrder(image.getSortOrder())
                .uploadedAt(image.getUploadedAt())
                .build();
    }
    /**
     * Создает DTO со списком изображений для конкретного продукта
     */
    public ProductImageListDTO toProductImageListDTO(Long productId, List<ProductImage> images) {
        return ProductImageListDTO.builder()
                .productId(productId)
                .images(toDTOList(images))
                .build();
    }

    /**
     * Конвертирует список entity в список DTO
     */
    public List<ProductImageDTO> toDTOList(List<ProductImage> images) {
        return images.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Конвертирует DTO для создания в entity
     * Обратите внимание: imageUrl и imageId будут установлены сервисом загрузки файлов,
     * а не прямо из DTO
     */
    public ProductImage toEntity(ProductImageCreateDTO dto, Product product, String imageUrl, String imageId) {
        if (dto == null) {
            return null;
        }

        return ProductImage.builder()
                .product(product)
                .imageType(dto.getImageType())
                .imageUrl(imageUrl)  // URL будет получен после загрузки файла
                .imageId(imageId)    // ID изображения будет получен после загрузки файла
                .alt(dto.getAlt())
                .sortOrder(dto.getSortOrder())
                .build();
    }

    /**
     * Обновляет существующий entity данными из DTO для обновления
     */
    public void updateEntityFromDTO(ProductImageUpdateDTO dto, ProductImage image) {
        if (dto == null || image == null) {
            return;
        }

        if (dto.getImageType() != null) {
            image.setImageType(dto.getImageType());
        }


        if (dto.getAlt() != null) {
            image.setAlt(dto.getAlt());
        }

        if (dto.getSortOrder() != null) {
            image.setSortOrder(dto.getSortOrder());
        }
    }
}