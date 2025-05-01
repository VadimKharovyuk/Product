package com.example.product.dto.ProductAttribute;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAttributeDTO {
    private Long id;
    private Long productId;
    private String color;
    private String size;
    private String material;
}