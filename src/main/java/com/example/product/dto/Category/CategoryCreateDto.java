package com.example.product.dto.Category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateDto {
    @NotBlank
    private String name;

    private String description;
    private MultipartFile image;
    private String slug;
    private Long parentId;
    private Integer sortOrder;
    private boolean active = true;
    private String metaKeywords;
    private String metaTitle;
}
