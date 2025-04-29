package com.example.product.maper;

import com.example.product.dto.BrandDto.BrandCreateRequest;
import com.example.product.dto.BrandDto.BrandDTO;
import com.example.product.dto.BrandDto.BrandListDTO;
import com.example.product.dto.BrandDto.BrandUpdateRequest;
import com.example.product.model.Brand;
import com.example.product.util.SlugUtil;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

@Component
public class BrandMapper {

    public BrandDTO toDTO(Brand brand) {
        if (brand == null) {
            return null;
        }

        return BrandDTO.builder()
                .id(brand.getId())
                .name(brand.getName())
                .description(brand.getDescription())
                .bannerUrl(brand.getBannerUrl())
                .bannerImageId(brand.getBannerImageId())
                .slug(brand.getSlug())
                .metaKeywords(brand.getMetaKeywords())
                .active(brand.isActive())
                .premium(brand.isPremium())
                .country(brand.getCountry())
                .foundedYear(brand.getFoundedYear())
                .productCount(brand.getProductCount())
                .sortOrder(brand.getSortOrder())
                .createdAt(brand.getCreatedAt())
                .updatedAt(brand.getUpdatedAt())
                .build();
    }

    public BrandListDTO toListDTO(Brand brand) {
        if (brand == null) {
            return null;
        }

        return BrandListDTO.builder()
                .id(brand.getId())
                .name(brand.getName())
                .slug(brand.getSlug())
                .bannerUrl(brand.getBannerUrl())
                .active(brand.isActive())
                .premium(brand.isPremium())
                .country(brand.getCountry())
                .productCount(brand.getProductCount())
                .sortOrder(brand.getSortOrder())
                .build();
    }

    public List<BrandListDTO> toListDTO(List<Brand> brands) {
        if (brands == null) {
            return Collections.emptyList();
        }

        return brands.stream()
                .map(this::toListDTO)
                .collect(Collectors.toList());
    }

    public Brand toEntity(BrandCreateRequest request) {
        if (request == null) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();

        return Brand.builder()
                .name(request.getName())
                .description(request.getDescription())
                .slug(request.getSlug() != null ? request.getSlug() : SlugUtil.generateSlug(request.getName()))
                .metaKeywords(request.getMetaKeywords())
                .active(request.isActive())
                .premium(request.isPremium())
                .country(request.getCountry())
                .foundedYear(request.getFoundedYear())
                .productCount(0) // По умолчанию у нового бренда 0 продуктов
                .sortOrder(request.getSortOrder())
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void updateEntityFromRequest(Brand brand, BrandUpdateRequest request) {
        if (request == null || brand == null) {
            return;
        }

        if (request.getName() != null) {
            brand.setName(request.getName());
            // Если имя изменилось и slug не указан явно, обновим его
            if (request.getSlug() == null) {
                brand.setSlug(SlugUtil.generateSlug(request.getName()));
            }
        }

        if (request.getDescription() != null) {
            brand.setDescription(request.getDescription());
        }

        if (request.getSlug() != null) {
            brand.setSlug(request.getSlug());
        }

        if (request.getMetaKeywords() != null) {
            brand.setMetaKeywords(request.getMetaKeywords());
        }

        if (request.getActive() != null) {
            brand.setActive(request.getActive());
        }

        if (request.getPremium() != null) {
            brand.setPremium(request.getPremium());
        }

        if (request.getCountry() != null) {
            brand.setCountry(request.getCountry());
        }

        if (request.getFoundedYear() != null) {
            brand.setFoundedYear(request.getFoundedYear());
        }

        if (request.getSortOrder() != null) {
            brand.setSortOrder(request.getSortOrder());
        }
        brand.setUpdatedAt(LocalDateTime.now());
    }
}