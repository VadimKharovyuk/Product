package com.example.product.maper;

import com.example.product.dto.Product.ProductCreateRequest;
import com.example.product.dto.Product.ProductDTO;
import com.example.product.dto.Product.ProductListDTO;
import com.example.product.dto.Product.ProductUpdateRequest;
import com.example.product.enums.ImageType;
import com.example.product.model.Product;
import com.example.product.model.ProductImage;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    private final CategoryMapper categoryMapper;
    private final BrandMapper brandMapper;
    private final ProductImageMapper imageMapper;
    private final ProductAttributeMapper attributeMapper;

    public ProductMapper(CategoryMapper categoryMapper, BrandMapper brandMapper,
                         ProductImageMapper imageMapper, ProductAttributeMapper attributeMapper) {
        this.categoryMapper = categoryMapper;
        this.brandMapper = brandMapper;
        this.imageMapper = imageMapper;
        this.attributeMapper = attributeMapper;
    }

    public Product toEntity(ProductCreateRequest dto) {
        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .sku(dto.getSku())
                .price(dto.getPrice())
                .stockQuantity(dto.getStockQuantity())
                .weight(dto.getWeight())
                .height(dto.getHeight())
                .width(dto.getWidth())
                .depth(dto.getDepth())
                .status(dto.getStatus())
                .featured(dto.isFeatured())
                .metaTitle(dto.getMetaTitle())
                .metaDescription(dto.getMetaDescription())
                .metaKeywords(dto.getMetaKeywords())
                .categories(new HashSet<>())
                .images(new java.util.ArrayList<>())
                .attributes(new java.util.ArrayList<>())
                .build();
    }

    public void updateProductFromDTO(Product product, ProductUpdateRequest dto) {
        if (dto.getName() != null) {
            product.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }
        if (dto.getSku() != null) {
            product.setSku(dto.getSku());
        }
        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }
        if (dto.getStockQuantity() != null) {
            product.setStockQuantity(dto.getStockQuantity());
        }
        if (dto.getWeight() != null) {
            product.setWeight(dto.getWeight());
        }
        if (dto.getHeight() != null) {
            product.setHeight(dto.getHeight());
        }
        if (dto.getWidth() != null) {
            product.setWidth(dto.getWidth());
        }
        if (dto.getDepth() != null) {
            product.setDepth(dto.getDepth());
        }
        if (dto.getStatus() != null) {
            product.setStatus(dto.getStatus());
        }
        product.setFeatured(dto.isFeatured());
        if (dto.getMetaTitle() != null) {
            product.setMetaTitle(dto.getMetaTitle());
        }
        if (dto.getMetaDescription() != null) {
            product.setMetaDescription(dto.getMetaDescription());
        }
        if (dto.getMetaKeywords() != null) {
            product.setMetaKeywords(dto.getMetaKeywords());
        }
    }

    public ProductDTO toDTO(Product product) {
        String mainImageUrl = null;
        if (!product.getImages().isEmpty()) {
            // Поиск главного изображения
            mainImageUrl = product.getImages().stream()
                    .filter(image -> image.getImageType() == ImageType.MAIN)
                    .findFirst()
                    .map(ProductImage::getImageUrl)
                    .orElseGet(() -> product.getImages().get(0).getImageUrl());
        }

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .sku(product.getSku())
                .slug(product.getSlug())
                .categories(product.getCategories().stream()
                        .map(categoryMapper::toListDto)
                        .collect(Collectors.toSet()))
                .images(product.getImages().stream()
                        .map(imageMapper::toDTO)
                        .collect(Collectors.toList()))
                .attributes(product.getAttributes().stream()
                        .map(attributeMapper::toDTO)
                        .collect(Collectors.toList()))
                .brand(product.getBrand() != null ? brandMapper.toDTO(product.getBrand()) : null)
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .weight(product.getWeight())
                .height(product.getHeight())
                .width(product.getWidth())
                .depth(product.getDepth())
                .status(product.getStatus())
                .featured(product.isFeatured())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .metaTitle(product.getMetaTitle())
                .metaDescription(product.getMetaDescription())
                .metaKeywords(product.getMetaKeywords())
                .mainImageUrl(mainImageUrl)
                .viewCount(product.getViewCount())
                .build();
    }

    public ProductListDTO toListDTO(Product product) {
        String mainImageUrl = null;
        if (!product.getImages().isEmpty()) {
            // Поиск главного изображения
            mainImageUrl = product.getImages().stream()
                    .filter(image -> image.getImageType() == ImageType.MAIN)
                    .findFirst()
                    .map(ProductImage::getImageUrl)
                    .orElseGet(() -> product.getImages().get(0).getImageUrl());
        }

        return ProductListDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .price(product.getPrice())
                .status(product.getStatus())
                .featured(product.isFeatured())
                .mainImageUrl(mainImageUrl)
                .brandId(product.getBrand() != null ? product.getBrand().getId() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
                .viewCount(product.getViewCount())
                .build();
    }
}