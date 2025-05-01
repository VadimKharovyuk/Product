package com.example.product.dto.ProductAttribute;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAttributeUpdateDTO {
    @NotNull(message = "ID атрибута не может быть пустым")
    private Long id;

    private String color;
    private String size;
    private String material;
}
