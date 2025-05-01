package com.example.product.dto.Product;



import com.example.product.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {
    private String name;
    private String description;
    private String sku;
    private Set<Long> categoryIds = new HashSet<>();
    private Long brandId;
    private BigDecimal price;
    private Integer stockQuantity;
    private Double weight;
    private Double height;
    private Double width;
    private Double depth;
    private ProductStatus status;
    private boolean featured;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
}
