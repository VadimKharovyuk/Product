package com.example.product.enums;

import lombok.Getter;

import lombok.Getter;

@Getter
public enum ImageType {
    MAIN("Главное изображение"),
    GALLERY("Изображение для галереи"),
    THUMBNAIL("Миниатюра"),
    ZOOM("Увеличенное изображение для зума"),
    VARIANT("Изображение варианта товара");

    private final String description;

    ImageType(String description) {
        this.description = description;
    }
}