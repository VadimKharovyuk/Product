package com.example.product.model;

import com.example.product.enums.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(unique = true)
    private String sku; // Уникальный идентификатор товара

    @Column(unique = true)
    private String slug; // URL-friendly имя

    @ManyToMany
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductAttribute> attributes = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand ;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private Integer stockQuantity;

    private BigDecimal weight;

    private String dimensions; // Можно разбить на height, width, depth

    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.ACTIVE;

    private boolean featured = false;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Дополнительные поля для SEO
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;


}
