package com.example.product.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "brands")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    private String description;

    private String bannerUrl;      // Баннер бренда
    private String bannerImageId;

    @Column(unique = true)
    private String slug;
    private String metaKeywords;

    private boolean active = true;
    private boolean premium = false;

    private String country;        // Страна происхождения
    private Integer foundedYear;   // Год основания

    @OneToMany(mappedBy = "brand")
    private List<Product> products = new ArrayList<>();

    private Integer productCount;  // Количество товаров бренда

    private Integer sortOrder;



    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}