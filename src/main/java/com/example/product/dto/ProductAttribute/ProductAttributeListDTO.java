package com.example.product.dto.ProductAttribute;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAttributeListDTO {
    private Long productId;
    private List<ProductAttributeDTO> attributes = new ArrayList<>();
}