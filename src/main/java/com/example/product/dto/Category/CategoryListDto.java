package com.example.product.dto.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryListDto {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Integer productCount;
    private String slug;
    private boolean isPopular;
    private Integer sortOrder;
    private boolean active;
    private boolean hasSubcategories;
}
