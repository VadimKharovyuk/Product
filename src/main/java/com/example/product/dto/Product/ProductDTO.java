package com.example.product.dto.Product;

import com.example.product.dto.BrandDto.BrandDTO;
import com.example.product.dto.Category.CategoryListDto;
import com.example.product.dto.ProductAttribute.ProductAttributeDTO;
import com.example.product.dto.ProductImage.ProductImageDTO;
import com.example.product.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private String sku;
    private String slug;
    private Set<CategoryListDto> categories = new HashSet<>();
    private List<ProductImageDTO> images = new ArrayList<>();
    private List<ProductAttributeDTO> attributes = new ArrayList<>();
    private BrandDTO brand;
    private BigDecimal price;
    private Integer stockQuantity;
    private Double weight;
    private Double height;
    private Double width;
    private Double depth;
    private ProductStatus status;
    private boolean featured;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private String mainImageUrl;
}