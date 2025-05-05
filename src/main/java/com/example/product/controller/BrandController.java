
package com.example.product.controller;
import com.example.product.dto.BrandDto.BrandCreateRequest;
import com.example.product.dto.BrandDto.BrandDTO;
import com.example.product.dto.BrandDto.BrandListDTO;
import com.example.product.dto.BrandDto.BrandUpdateRequest;
import com.example.product.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class BrandController {

    private final BrandService brandService;

    // Публичные эндпоинты для брендов

    @GetMapping("/public/brands")
    public ResponseEntity<List<BrandListDTO>> getAllPublicBrands(
            @RequestParam(required = false) Boolean active) {

        // Для публичного API всегда возвращаем только активные бренды
        List<BrandListDTO> brands = brandService.getBrandsByActive(active != null ? active : true);
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/public/brands/paginated")
    public ResponseEntity<Page<BrandListDTO>> getPaginatedPublicBrands(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<BrandListDTO> brands = brandService.getBrandsPaginated(pageRequest);

        return ResponseEntity.ok(brands);
    }

    @GetMapping("/public/brands/search")
    public ResponseEntity<List<BrandListDTO>> searchPublicBrands(
            @RequestParam String query) {
        return ResponseEntity.ok(brandService.searchBrands(query));
    }

    @GetMapping("/public/brands/premium")
    public ResponseEntity<List<BrandListDTO>> getPublicPremiumBrands() {
        return ResponseEntity.ok(brandService.getBrandsByPremium(true));
    }

    @GetMapping("/public/brands/{id}")
    public ResponseEntity<BrandDTO> getPublicBrandById(@PathVariable Long id) {
        return brandService.getBrandById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/public/brands/slug/{slug}")
    public ResponseEntity<BrandDTO> getPublicBrandBySlug(@PathVariable String slug) {
        return brandService.getBrandBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/public/brands/count")
    public ResponseEntity<Long> getPublicBrandCount() {
        long count = brandService.getBrandCount();
        return ResponseEntity.ok(count);
    }

    // Административные эндпоинты для брендов (требуют аутентификации и роли ADMIN)

    @GetMapping("/admin/brands")
    public ResponseEntity<List<BrandListDTO>> getAllAdminBrands(
            @RequestParam(required = false) Boolean active) {

        List<BrandListDTO> brands;
        if (active != null) {
            brands = brandService.getBrandsByActive(active);
        } else {
            brands = brandService.getAllBrands();
        }

        return ResponseEntity.ok(brands);
    }

    @PostMapping("/admin/brands")
    public ResponseEntity<BrandDTO> createBrand(
            @Valid @RequestBody BrandCreateRequest request) {
        try {
            BrandDTO createdBrand = brandService.createBrand(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBrand);
        } catch (IOException e) {
            log.error("Ошибка при создании бренда", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/admin/brands/{id}")
    public ResponseEntity<BrandDTO> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody BrandUpdateRequest request) {
        try {
            BrandDTO updatedBrand = brandService.updateBrand(id, request);
            return ResponseEntity.ok(updatedBrand);
        } catch (IOException e) {
            log.error("Ошибка при обновлении бренда", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (RuntimeException e) {
            log.error("Бренд не найден: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/admin/brands/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        try {
            brandService.deleteBrand(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Невозможно удалить бренд")) {
                log.error("Невозможно удалить бренд: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            log.error("Бренд не найден: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/admin/brands/{id}/banner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BrandDTO> uploadBannerImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            BrandDTO brand = brandService.uploadBannerImage(id, file);
            return ResponseEntity.ok(brand);
        } catch (IOException e) {
            log.error("Ошибка при загрузке баннера для бренда", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (RuntimeException e) {
            log.error("Бренд не найден: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/admin/brands/{id}/banner")
    public ResponseEntity<Void> deleteBannerImage(@PathVariable Long id) {
        try {
            brandService.deleteBannerImage(id);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            log.error("Ошибка при удалении баннера для бренда", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (RuntimeException e) {
            log.error("Бренд не найден: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/brands/category/{categoryId}")
    public ResponseEntity<List<BrandListDTO>> getBrandsByCategory(@PathVariable Long categoryId) {
        log.info("Запрос на получение брендов для категории с ID: {}", categoryId);
        List<BrandListDTO> brands = brandService.getBrandsByCategory(categoryId);
        return ResponseEntity.ok(brands);
    }

}