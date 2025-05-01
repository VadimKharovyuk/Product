package com.example.product.maper;

import com.example.product.dto.ProductAttribute.ProductAttributeCreateDTO;
import com.example.product.dto.ProductAttribute.ProductAttributeDTO;
import com.example.product.dto.ProductAttribute.ProductAttributeListDTO;
import com.example.product.dto.ProductAttribute.ProductAttributeUpdateDTO;
import com.example.product.model.Product;
import com.example.product.model.ProductAttribute;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductAttributeMapper {

    /**
     * Конвертирует entity в DTO
     */
    public ProductAttributeDTO toDTO(ProductAttribute attribute) {
        if (attribute == null) {
            return null;
        }

        return ProductAttributeDTO.builder()
                .id(attribute.getId())
                .productId(attribute.getProduct() != null ? attribute.getProduct().getId() : null)
                .color(attribute.getColor()) // Lombok генерирует getColor() для поля Color
                .size(attribute.getSize())   // Lombok генерирует getSize() для поля Size
                .material(attribute.getMaterial()) // Lombok генерирует getMaterial() для поля Material
                .build();
    }

    /**
     * Конвертирует список entity в список DTO
     */
    public List<ProductAttributeDTO> toDTOList(List<ProductAttribute> attributes) {
        return attributes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Создает DTO со списком атрибутов для конкретного продукта
     */
    public ProductAttributeListDTO toProductAttributeListDTO(Long productId, List<ProductAttribute> attributes) {
        return ProductAttributeListDTO.builder()
                .productId(productId)
                .attributes(toDTOList(attributes))
                .build();
    }

    /**
     * Конвертирует DTO для создания в entity
     */
    public ProductAttribute toEntity(ProductAttributeCreateDTO dto, Product product) {
        if (dto == null) {
            return null;
        }

        return ProductAttribute.builder()
                .product(product)
                .color(dto.getColor())
                .size(dto.getSize())
                .material(dto.getMaterial())
                .build();
    }

    /**
     * Обновляет существующий entity данными из DTO для обновления
     */
    public void updateEntityFromDTO(ProductAttributeUpdateDTO dto, ProductAttribute attribute) {
        if (dto == null || attribute == null) {
            return;
        }

        if (dto.getColor() != null) {
            attribute.setColor(dto.getColor());
        }

        if (dto.getSize() != null) {
            attribute.setSize(dto.getSize());
        }

        if (dto.getMaterial() != null) {
            attribute.setMaterial(dto.getMaterial());
        }
    }
}