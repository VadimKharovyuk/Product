package com.example.product.config;

import com.example.product.Exception.ResourceNotFoundException;
import com.example.product.controller.ProductAttributeController;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Специальный обработчик исключений для контроллера атрибутов продуктов.
 * Имеет более высокий приоритет, чем глобальный обработчик.
 */
@RestControllerAdvice(basePackageClasses = ProductAttributeController.class)
@Order(1) // Более высокий приоритет, чем глобальный обработчик
@Slf4j
public class ProductAttributeControllerAdvice {

    /**
     * Обрабатывает исключения ResourceNotFoundException, вызванные в контроллере атрибутов продуктов
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<AttributeErrorResponse> handleAttributeNotFound(ResourceNotFoundException ex) {
        log.error("Атрибут не найден: {}", ex.getMessage());

        AttributeErrorResponse response = new AttributeErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "ATTRIBUTE_NOT_FOUND",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Специальный формат ответа для ошибок, связанных с атрибутами
     */
    @Getter
    public static class AttributeErrorResponse {
        private final int status;
        private final String code;
        private final String message;
        private final LocalDateTime timestamp;
        private final String resource = "product_attribute";

        public AttributeErrorResponse(int status, String code, String message, LocalDateTime timestamp) {
            this.status = status;
            this.code = code;
            this.message = message;
            this.timestamp = timestamp;
        }

    }
}