package com.example.product.dto.BrandDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDTO {
    private Long id;

    @NotBlank(message = "Название бренда не может быть пустым")
    private String name;

    private String description;
    private String bannerUrl;
    private String bannerImageId;
    private String slug;
    private String metaKeywords;
    private boolean active;
    private boolean premium;
    private String country;
    private Integer foundedYear;
    private Integer productCount;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}