package com.example.product.controller;

import com.example.product.dto.ProductImage.ProductImageCreateDTO;
import com.example.product.dto.ProductImage.ProductImageDTO;
import com.example.product.dto.ProductImage.ProductImageListDTO;
import com.example.product.dto.ProductImage.ProductImageUpdateDTO;
import com.example.product.service.ProductImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/product-images")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Изображения продуктов", description = "API для управления изображениями продуктов")
public class ProductImageController {

    private final ProductImageService productImageService;

    @GetMapping("/product/{productId}")
    @Operation(summary = "Получить все изображения продукта", description = "Возвращает список всех изображений для указанного продукта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изображения успешно получены"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден")
    })
    public ResponseEntity<ProductImageListDTO> getProductImages(
            @Parameter(description = "ID продукта", required = true)
            @PathVariable Long productId) {
        return ResponseEntity.ok(productImageService.getProductImages(productId));
    }

    @GetMapping("/{imageId}")
    @Operation(summary = "Получить изображение по ID", description = "Возвращает изображение по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изображение успешно получено"),
            @ApiResponse(responseCode = "404", description = "Изображение не найдено")
    })
    public ResponseEntity<ProductImageDTO> getImageById(
            @Parameter(description = "ID изображения", required = true)
            @PathVariable Long imageId) {
        return ResponseEntity.ok(productImageService.getImageById(imageId));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузить новое изображение", description = "Загружает и создает новое изображение для продукта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Изображение успешно загружено"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден"),
            @ApiResponse(responseCode = "413", description = "Превышен максимальный размер файла")
    })
    public ResponseEntity<ProductImageDTO> uploadImage(
            @Parameter(description = "ID продукта", required = true)
            @RequestParam("productId") Long productId,

            @Parameter(description = "Файл изображения", required = true)
            @RequestParam("imageFile") MultipartFile imageFile,

            @Parameter(description = "Тип изображения")
            @RequestParam(value = "imageType", required = false) String imageType,

            @Parameter(description = "Альтернативный текст")
            @RequestParam(value = "alt", required = false) String alt,

            @Parameter(description = "Порядок сортировки")
            @RequestParam(value = "sortOrder", required = false) Integer sortOrder) throws IOException {

        ProductImageCreateDTO createDTO = ProductImageCreateDTO.builder()
                .productId(productId)
                .imageFile(imageFile)
                .imageType(imageType != null ? com.example.product.enums.ImageType.valueOf(imageType) : null)
                .alt(alt)
                .sortOrder(sortOrder)
                .build();

        return new ResponseEntity<>(productImageService.createProductImage(createDTO), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{imageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновить изображение", description = "Обновляет существующее изображение по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изображение успешно обновлено"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Изображение не найдено"),
            @ApiResponse(responseCode = "413", description = "Превышен максимальный размер файла")
    })
    public ResponseEntity<ProductImageDTO> updateImage(
            @Parameter(description = "ID изображения", required = true)
            @PathVariable Long imageId,

            @Parameter(description = "Файл изображения")
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,

            @Parameter(description = "Тип изображения")
            @RequestParam(value = "imageType", required = false) String imageType,

            @Parameter(description = "Альтернативный текст")
            @RequestParam(value = "alt", required = false) String alt,

            @Parameter(description = "Порядок сортировки")
            @RequestParam(value = "sortOrder", required = false) Integer sortOrder) throws IOException {

        ProductImageUpdateDTO updateDTO = ProductImageUpdateDTO.builder()
                .id(imageId)
                .imageFile(imageFile)
                .imageType(imageType != null ? com.example.product.enums.ImageType.valueOf(imageType) : null)
                .alt(alt)
                .sortOrder(sortOrder)
                .build();

        return ResponseEntity.ok(productImageService.updateProductImage(imageId, updateDTO));
    }

    @DeleteMapping("/{imageId}")
    @Operation(summary = "Удалить изображение", description = "Удаляет изображение по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Изображение успешно удалено"),
            @ApiResponse(responseCode = "404", description = "Изображение не найдено")
    })
    public ResponseEntity<Void> deleteImage(
            @Parameter(description = "ID изображения", required = true)
            @PathVariable Long imageId) {
        productImageService.deleteProductImage(imageId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/order/{productId}")
    @Operation(summary = "Изменить порядок изображений", description = "Изменяет порядок сортировки изображений для продукта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Порядок успешно изменен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Продукт или изображение не найдено")
    })
    public ResponseEntity<Void> updateImageOrder(
            @Parameter(description = "ID продукта", required = true)
            @PathVariable Long productId,

            @Parameter(description = "Список ID изображений в нужном порядке", required = true)
            @RequestBody List<Long> imageIds) {
        productImageService.updateImageOrder(productId, imageIds);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/main/{imageId}")
    @Operation(summary = "Установить главное изображение", description = "Устанавливает указанное изображение как главное для продукта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Главное изображение успешно установлено"),
            @ApiResponse(responseCode = "404", description = "Изображение не найдено")
    })
    public ResponseEntity<ProductImageDTO> setMainImage(
            @Parameter(description = "ID изображения", required = true)
            @PathVariable Long imageId) {
        return ResponseEntity.ok(productImageService.setMainProductImage(imageId));
    }
}