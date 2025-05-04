package com.example.product.dto.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularCategoryDto {
    private Long id;
    private String name;
    private String imageUrl;
    private Integer productCount;
    private String slug;

    // Можно добавить показатель популярности
    private Integer popularityScore;

    // Дополнительные поля, которые могут быть полезны
    private BigDecimal minPrice; // Минимальная цена товара в категории
    private BigDecimal maxPrice; // Максимальная цена товара в категории
    private boolean hasDiscount; // Есть ли товары со скидкой
}