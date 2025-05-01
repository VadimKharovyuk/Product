package com.example.product.dto.Product;


import com.example.product.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductListDTO {
    private Long id;
    private String name;
    private String slug;
    private BigDecimal price;
    private ProductStatus status;
    private boolean featured;
    private String mainImageUrl;
    private Long brandId;
    private String brandName;
}