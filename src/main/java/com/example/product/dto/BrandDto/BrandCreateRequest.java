package com.example.product.dto.BrandDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandCreateRequest {
    @NotBlank(message = "Название бренда не может быть пустым")
    private String name;

    private String description;
    private MultipartFile banner; // Для загрузки баннера
    private String slug;
    private String metaKeywords;
    private boolean active = true;
    private boolean premium = false;
    private String country;
    private Integer foundedYear;
    private Integer sortOrder;
}