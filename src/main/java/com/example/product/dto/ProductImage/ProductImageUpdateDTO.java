package com.example.product.dto.ProductImage;
import com.example.product.enums.ImageType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageUpdateDTO {
    @NotNull(message = "ID изображения не может быть пустым")
    private Long id;

    private ImageType imageType;

    private MultipartFile imageFile;

    private String imageId;

    private String alt;

    private Integer sortOrder;
}