package com.example.product.maper;


import com.example.product.dto.Category.*;
import com.example.product.model.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {


    public Category toEntity(CategoryCreateDto dto) {
        return Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                // imageUrl будет установлен после обработки MultipartFile
                .slug(dto.getSlug())
                .sortOrder(dto.getSortOrder())
                .active(dto.isActive())
                .metaKeywords(dto.getMetaKeywords())
                .metaTitle(dto.getMetaTitle())
                // Поля, которые не устанавливаются при создании
                .viewCount(0)
                .cartAddCount(0)
                .orderCount(0)
                .lastMonthOrderCount(0)
                .lastWeekOrderCount(0)
                .totalRevenue(java.math.BigDecimal.ZERO)
                .isPopular(false)
                .build();
    }

    /**
     * Обновляет существующую сущность Category данными из DTO
     * @param entity существующая сущность для обновления
     * @param dto DTO с новыми данными
     */
    public void updateEntityFromDto(Category entity, CategoryCreateDto dto) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        // imageUrl обрабатывается отдельно после обработки MultipartFile
        entity.setSlug(dto.getSlug());
        entity.setSortOrder(dto.getSortOrder());
        entity.setActive(dto.isActive());
        entity.setMetaKeywords(dto.getMetaKeywords());
        entity.setMetaTitle(dto.getMetaTitle());
        // Не обновляем счетчики и статистику здесь
    }

    /**
     * Конвертирует Category в детальное представление CategoryDetailsDto
     * @param entity сущность Category
     * @return dto с полной информацией о категории
     */
    public CategoryDetailsDto toDetailsDto(Category entity) {
        if (entity == null) {
            return null;
        }

        CategoryDetailsDto dto = CategoryDetailsDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .productCount(entity.getProductCount())
                .imageUrl(entity.getImageUrl())
                .imageId(entity.getImageId())
                .viewCount(entity.getViewCount())
                .cartAddCount(entity.getCartAddCount())
                .orderCount(entity.getOrderCount())
                .lastMonthOrderCount(entity.getLastMonthOrderCount())
                .lastWeekOrderCount(entity.getLastWeekOrderCount())
                .totalRevenue(entity.getTotalRevenue())
                .lastOrderDate(entity.getLastOrderDate())
                .isPopular(entity.isPopular())
                .slug(entity.getSlug())
                .metaKeywords(entity.getMetaKeywords())
                .metaTitle(entity.getMetaTitle())
                .sortOrder(entity.getSortOrder())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();

        // Устанавливаем родительскую категорию, если она есть
        if (entity.getParent() != null) {
            dto.setParent(toShortDto(entity.getParent()));
        }

        // Устанавливаем подкатегории
        if (entity.getSubcategories() != null && !entity.getSubcategories().isEmpty()) {
            dto.setSubcategories(entity.getSubcategories().stream()
                    .map(this::toShortDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    /**
     * Конвертирует Category в представление для списка CategoryListDto
     * @param entity сущность Category
     * @return dto для списка категорий
     */
    public CategoryListDto toListDto(Category entity) {
        if (entity == null) {
            return null;
        }

        boolean hasSubcategories = entity.getSubcategories() != null && !entity.getSubcategories().isEmpty();

        return CategoryListDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .imageUrl(entity.getImageUrl())
                .productCount(entity.getProductCount())
                .slug(entity.getSlug())
                .isPopular(entity.isPopular())
                .sortOrder(entity.getSortOrder())
                .active(entity.isActive())
                .hasSubcategories(hasSubcategories)
                .build();
    }

    /**
     * Конвертирует Category в краткое представление CategoryShortDto
     * @param entity сущность Category
     * @return краткое dto категории
     */
    public CategoryShortDto toShortDto(Category entity) {
        if (entity == null) {
            return null;
        }

        return CategoryShortDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .slug(entity.getSlug())
                .build();
    }

    /**
     * Конвертирует список сущностей Category в список CategoryListDto
     * @param entities список сущностей
     * @return список dto для отображения в списке
     */
    public List<CategoryListDto> toListDto(List<Category> entities) {
        if (entities == null) {
            return new ArrayList<>();
        }

        return entities.stream()
                .map(this::toListDto)
                .collect(Collectors.toList());
    }

    public PopularCategoryDto toPopularCategoryDto(Category category) {
        if (category == null) {
            return null;
        }

        Integer popularityScore = category.getViewCount() +
                (category.getCartAddCount() * 3) +
                (category.getOrderCount() * 5);
        return PopularCategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .imageUrl(category.getImageUrl())
                .productCount(category.getProductCount())
                .slug(category.getSlug())
                .popularityScore(popularityScore)
//                 Эти поля нужно будет получать из связанных товаров
//                 .minPrice(getMinPriceForCategory(category.getId()))
//                 .maxPrice(getMaxPriceForCategory(category.getId()))
//                 .hasDiscount(hasDiscountedProductsInCategory(category.getId()))
                .build();
    }
}
