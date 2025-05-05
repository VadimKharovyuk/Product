package com.example.product.controller;

import com.example.product.dto.BrandDto.BrandListDTO;
import com.example.product.dto.Category.*;
import com.example.product.service.BrandService;
import com.example.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Category Controller", description = "API для управления категориями товаров")
public class CategoryController {

    private final CategoryService categoryService;
    private final BrandService brandService;

    @Operation(
            summary = "Создание новой категории",
            description = "Создает новую категорию с возможностью загрузки изображения"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Категория успешно создана",
            content = @Content(schema = @Schema(implementation = CategoryDetailsDto.class))
    )



    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryDetailsDto> createCategory(
            @ModelAttribute @Valid CategoryCreateDto categoryDto,
            @RequestPart(name = "imageFile", required = false) MultipartFile imageFile
    ) {
        try {
            log.info("Запрос на создание новой категории: {}", categoryDto.getName());
            CategoryDetailsDto createdCategory = categoryService.createCategory(categoryDto, imageFile);
            return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
        } catch (IOException e) {
            log.error("Ошибка при создании категории: {}", e.getMessage());
            throw new RuntimeException("Ошибка при загрузке изображения: " + e.getMessage());
        }
    }

    // Дополнительный метод для создания категории без изображения
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDetailsDto> createCategoryWithoutImage(
            @RequestBody @Valid CategoryCreateDto categoryDto
    ) {
        try {
            log.info("Запрос на создание новой категории без изображения: {}", categoryDto.getName());
            CategoryDetailsDto createdCategory = categoryService.createCategory(categoryDto, null);
            return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
        } catch (IOException e) {
            log.error("Ошибка при создании категории: {}", e.getMessage());
            throw new RuntimeException("Непредвиденная ошибка: " + e.getMessage());
        }
    }




    @Operation(
            summary = "Обновление существующей категории",
            description = "Обновляет существующую категорию с возможностью замены изображения"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Категория успешно обновлена",
            content = @Content(schema = @Schema(implementation = CategoryDetailsDto.class))
    )
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryDetailsDto> updateCategory(
            @PathVariable Long id,
            @RequestPart("data") @Valid CategoryCreateDto categoryDto,
            @RequestPart(name = "image", required = false) MultipartFile image
    ) {
        try {
            log.info("Запрос на обновление категории с ID {}: {}", id, categoryDto.getName());
            CategoryDetailsDto updatedCategory = categoryService.updateCategory(id, categoryDto, image);
            return ResponseEntity.ok(updatedCategory);
        } catch (IOException e) {
            log.error("Ошибка при обновлении категории: {}", e.getMessage());
            throw new RuntimeException("Ошибка при загрузке изображения: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Получение категории по ID",
            description = "Возвращает детальную информацию о категории по её ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Категория найдена",
            content = @Content(schema = @Schema(implementation = CategoryDetailsDto.class))
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDetailsDto> getCategoryById(@PathVariable Long id) {
        log.info("Запрос на получение категории по ID: {}", id);
        CategoryDetailsDto category = categoryService.getCategoryById(id);
        categoryService.incrementViewCount(id); // Увеличиваем счетчик просмотров
        return ResponseEntity.ok(category);
    }

    @Operation(
            summary = "Получение списка корневых категорий",
            description = "Возвращает список всех категорий верхнего уровня (без родителя)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список категорий",
            content = @Content(schema = @Schema(implementation = CategoryListDto.class))
    )
    @GetMapping
    public ResponseEntity<List<CategoryListDto>> getRootCategories() {
        log.info("Запрос на получение корневых категорий");
        List<CategoryListDto> categories = categoryService.getRootCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(
            summary = "Получение подкатегорий",
            description = "Возвращает список подкатегорий для указанной родительской категории"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список подкатегорий",
            content = @Content(schema = @Schema(implementation = CategoryListDto.class))
    )
    @GetMapping("/{id}/subcategories")
    public ResponseEntity<List<CategoryListDto>> getSubcategories(@PathVariable Long id) {
        log.info("Запрос на получение подкатегорий для категории с ID: {}", id);
        List<CategoryListDto> subcategories = categoryService.getSubcategories(id);
        return ResponseEntity.ok(subcategories);
    }

    @Operation(
            summary = "Удаление категории",
            description = "Удаляет категорию по указанному ID"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Категория успешно удалена"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("Запрос на удаление категории с ID: {}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Поиск категорий",
            description = "Ищет категории по имени или описанию"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список найденных категорий",
            content = @Content(schema = @Schema(implementation = CategoryListDto.class))
    )
    @GetMapping("/search")
    public ResponseEntity<List<CategoryListDto>> searchCategories(@RequestParam String query) {
        log.info("Запрос на поиск категорий по запросу: {}", query);
        List<CategoryListDto> foundCategories = categoryService.searchCategories(query);
        return ResponseEntity.ok(foundCategories);
    }

    @Operation(
            summary = "Обновление статуса популярности",
            description = "Устанавливает или снимает флаг популярности категории"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Статус популярности успешно обновлен"
    )
    @PatchMapping("/{id}/popular")
    public ResponseEntity<Void> updatePopularStatus(
            @PathVariable Long id,
            @RequestParam boolean popular
    ) {
        log.info("Запрос на обновление статуса популярности для категории {}: {}", id, popular);
        categoryService.updatePopularStatus(id, popular);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Получение общего количества категорий",
            description = "Возвращает общее количество категорий в системе"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Количество категорий",
            content = @Content(schema = @Schema(implementation = Long.class))
    )
    @GetMapping("/count")
    public ResponseEntity<Long> getCategoriesCount() {
        log.info("Запрос на получение общего количества категорий");
        long count = categoryService.getCategoriesCount();
        return ResponseEntity.ok(count);
    }


    @GetMapping("/public/popular")
    public ResponseEntity<List<PopularCategoryDto>> getPopularCategories() {
        log.info("Запрос на получение популярных категорий");
        List<PopularCategoryDto> popularCategoryList = categoryService.getPopularCategories();
        log.info("Количество популярных категорий: " + popularCategoryList.size());
        return ResponseEntity.ok(popularCategoryList);
    }

    ///Метод для получения категории по slug - нужен для удобных URL и навигации:

    @GetMapping("/slug/{slug}")
    public ResponseEntity<CategoryDetailsDto> getCategoryBySlug(@PathVariable String slug) {
        log.info("Запрос на получение категории по slug: {}", slug);
        CategoryDetailsDto category = categoryService.getCategoryBySlug(slug);
        if (category != null) {
            categoryService.incrementViewCount(category.getId()); // Увеличиваем счетчик просмотров
            return ResponseEntity.ok(category);
        }
        return ResponseEntity.notFound().build();
    }

    ///Метод для получения "хлебных крошек" - отображение пути от корневой категории до текущей:
    @GetMapping("/{id}/breadcrumbs")
    public ResponseEntity<List<CategoryListDto>> getCategoryBreadcrumbs(@PathVariable Long id) {
        log.info("Запрос на получение хлебных крошек для категории с ID: {}", id);
        List<CategoryListDto> breadcrumbs = categoryService.getCategoryBreadcrumbs(id);
        return ResponseEntity.ok(breadcrumbs);
    }

    ///Метод для получения популярных брендов в категории - нужен для быстрого доступа к популярным брендам:
    @GetMapping("/{id}/popular-brands")
    public ResponseEntity<List<BrandListDTO>> getPopularBrandsByCategory(@PathVariable Long id,
                                                                         @RequestParam(defaultValue = "5") int limit) {
        log.info("Запрос на получение популярных брендов для категории с ID: {}", id);
        List<BrandListDTO> popularBrands = brandService.getPopularBrandsByCategory(id, limit);
        return ResponseEntity.ok(popularBrands);
    }


///Получение категорий по уровню вложенности - полезно для построения меню:
    @GetMapping("/by-level")
    public ResponseEntity<List<CategoryListDto>> getCategoriesByLevel(@RequestParam int level) {
        log.info("Запрос на получение категорий по уровню вложенности: {}", level);
        List<CategoryListDto> categories = categoryService.getCategoriesByLevel(level);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/public/tree")
    public ResponseEntity<List<CategoryTreeDto>> getCategoryTree() {
        log.info("Запрос на получение древовидной структуры категорий");
        List<CategoryTreeDto> categoryTree = categoryService.getCategoryTree();
        return ResponseEntity.ok(categoryTree);
    }

    @PostMapping("/{id}/view")
    ResponseEntity<Void> incrementViewCount(@PathVariable Long id) {
        categoryService.incrementViewCount(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/shortInfo/{id}")
    public ResponseEntity<CategoryListDto> getCategoryShortInfo(@PathVariable Long id) {
        log.info("Запрос на получение краткой информации о категории с ID: {}", id);
        CategoryListDto category = categoryService.getCategoryShortInfo(id);
        return ResponseEntity.ok(category);
    }

    }
