package com.example.product.dto.ProductImage;

import com.example.product.enums.ImageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageCreateDTO {
    private Long productId;

    private ImageType imageType = ImageType.GALLERY;

    @NotNull(message = "Файл изображения не может быть пустым")
    private MultipartFile imageFile;  // Вместо String imageUrl

    private String alt;

    private Integer sortOrder;
}