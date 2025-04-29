package com.example.product.dto.BrandDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandUpdateRequest {
    private String name;
    private String description;
    private MultipartFile banner;
    private String slug;
    private String metaKeywords;
    private Boolean active;
    private Boolean premium;
    private String country;
    private Integer foundedYear;
    private Integer sortOrder;
}