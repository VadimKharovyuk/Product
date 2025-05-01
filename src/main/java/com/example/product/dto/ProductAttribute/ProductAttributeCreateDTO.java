package com.example.product.dto.ProductAttribute;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAttributeCreateDTO {
    private Long productId;

    @NotBlank(message = "Цвет не может быть пустым")
    private String color;

    @NotBlank(message = "Размер не может быть пустым")
    private String size;

    @NotBlank(message = "Материал не может быть пустым")
    private String material;
}