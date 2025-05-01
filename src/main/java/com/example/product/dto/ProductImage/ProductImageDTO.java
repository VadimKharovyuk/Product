package com.example.product.dto.ProductImage;
import com.example.product.enums.ImageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDTO {
    private Long id;
    private Long productId;
    private ImageType imageType;
    private String imageUrl;
    private String imageId;
    private String alt;
    private Integer sortOrder;
    private LocalDateTime uploadedAt;
}