package com.example.product.dto.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDetailsDto {
    private Long id;
    private String name;
    private String description;
    private Integer productCount;
    private String imageUrl;
    private String imageId;

    // Счетчики и статистика
    private Integer viewCount;
    private Integer cartAddCount;
    private Integer orderCount;
    private Integer lastMonthOrderCount;
    private Integer lastWeekOrderCount;
    private BigDecimal totalRevenue;
    private LocalDateTime lastOrderDate;
    private boolean isPopular;

    private String slug;
    private String metaKeywords;
    private String metaTitle;

    // Информация о родительской категории
    private CategoryShortDto parent;

    // Подкатегории
    private List<CategoryShortDto> subcategories = new ArrayList<>();

    private Integer sortOrder;
    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
