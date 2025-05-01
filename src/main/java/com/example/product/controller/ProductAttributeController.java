package com.example.product.controller;

import com.example.product.config.GlobalExceptionHandler;
import com.example.product.dto.ProductAttribute.ProductAttributeCreateDTO;
import com.example.product.dto.ProductAttribute.ProductAttributeDTO;
import com.example.product.dto.ProductAttribute.ProductAttributeListDTO;
import com.example.product.dto.ProductAttribute.ProductAttributeUpdateDTO;
import com.example.product.service.ProductAttributeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/attributes")
@RequiredArgsConstructor
@Tag(name = "Атрибуты продуктов", description = "API для управления атрибутами продуктов")
public class ProductAttributeController {

    private final ProductAttributeService productAttributeService;

    @GetMapping("/product/{productId}")
    @Operation(summary = "Получить все атрибуты продукта", description = "Возвращает список всех атрибутов для указанного продукта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Атрибуты успешно получены"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<ProductAttributeListDTO> getProductAttributes(
            @Parameter(description = "ID продукта", required = true)
            @PathVariable Long productId) {
        return ResponseEntity.ok(productAttributeService.getProductAttributes(productId));
    }

    @GetMapping("/{attributeId}")
    @Operation(summary = "Получить атрибут по ID", description = "Возвращает атрибут по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Атрибут успешно получен"),
            @ApiResponse(responseCode = "404", description = "Атрибут не найден",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<ProductAttributeDTO> getAttributeById(
            @Parameter(description = "ID атрибута", required = true)
            @PathVariable Long attributeId) {
        return ResponseEntity.ok(productAttributeService.getAttributeById(attributeId));
    }

    @PostMapping
    @Operation(summary = "Создать новый атрибут", description = "Создает новый атрибут для продукта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Атрибут успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Продукт не найден",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<ProductAttributeDTO> createAttribute(
            @Parameter(description = "Данные для создания атрибута", required = true)
            @Valid @RequestBody ProductAttributeCreateDTO createDTO) {
        return new ResponseEntity<>(productAttributeService.createAttribute(createDTO), HttpStatus.CREATED);
    }

    @PostMapping("/batch/{productId}")
    @Operation(summary = "Создать несколько атрибутов", description = "Создает несколько атрибутов для продукта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Атрибуты успешно созданы"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Продукт не найден",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<List<ProductAttributeDTO>> createAttributes(
            @Parameter(description = "ID продукта", required = true)
            @PathVariable Long productId,
            @Parameter(description = "Список атрибутов для создания", required = true)
            @Valid @RequestBody List<ProductAttributeCreateDTO> createDTOs) {
        return new ResponseEntity<>(productAttributeService.createAttributes(productId, createDTOs), HttpStatus.CREATED);
    }

    @PutMapping("/{attributeId}")
    @Operation(summary = "Обновить атрибут", description = "Обновляет существующий атрибут по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Атрибут успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Атрибут не найден",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<ProductAttributeDTO> updateAttribute(
            @Parameter(description = "ID атрибута", required = true)
            @PathVariable Long attributeId,
            @Parameter(description = "Данные для обновления атрибута", required = true)
            @Valid @RequestBody ProductAttributeUpdateDTO updateDTO) {
        return ResponseEntity.ok(productAttributeService.updateAttribute(attributeId, updateDTO));
    }

    @DeleteMapping("/{attributeId}")
    @Operation(summary = "Удалить атрибут", description = "Удаляет атрибут по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Атрибут успешно удален"),
            @ApiResponse(responseCode = "404", description = "Атрибут не найден",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteAttribute(
            @Parameter(description = "ID атрибута", required = true)
            @PathVariable Long attributeId) {
        productAttributeService.deleteAttribute(attributeId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/product/{productId}")
    @Operation(summary = "Удалить все атрибуты продукта", description = "Удаляет все атрибуты указанного продукта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Атрибуты успешно удалены"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteAllProductAttributes(
            @Parameter(description = "ID продукта", required = true)
            @PathVariable Long productId) {
        productAttributeService.deleteAllProductAttributes(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/color/{color}")
    @Operation(summary = "Поиск атрибутов по цвету", description = "Возвращает список атрибутов с указанным цветом")
    public ResponseEntity<List<ProductAttributeDTO>> findAttributesByColor(
            @Parameter(description = "Цвет для поиска", required = true)
            @PathVariable String color) {
        return ResponseEntity.ok(productAttributeService.findAttributesByColor(color));
    }

    @GetMapping("/search/size/{size}")
    @Operation(summary = "Поиск атрибутов по размеру", description = "Возвращает список атрибутов с указанным размером")
    public ResponseEntity<List<ProductAttributeDTO>> findAttributesBySize(
            @Parameter(description = "Размер для поиска", required = true)
            @PathVariable String size) {
        return ResponseEntity.ok(productAttributeService.findAttributesBySize(size));
    }

    @GetMapping("/search/material/{material}")
    @Operation(summary = "Поиск атрибутов по материалу", description = "Возвращает список атрибутов с указанным материалом")
    public ResponseEntity<List<ProductAttributeDTO>> findAttributesByMaterial(
            @Parameter(description = "Материал для поиска", required = true)
            @PathVariable String material) {
        return ResponseEntity.ok(productAttributeService.findAttributesByMaterial(material));
    }
}