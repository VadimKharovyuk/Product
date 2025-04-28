package com.example.product.enums;

import lombok.Getter;

import lombok.Getter;

@Getter
public enum ProductStatus {

    ACTIVE("Активный товар"),
    INACTIVE("Неактивный товар"),
    OUT_OF_STOCK("Нет в наличии"),
    DISCONTINUED("Снят с продажи");

    private final String description;

    ProductStatus(String description) {
        this.description = description;
    }
}