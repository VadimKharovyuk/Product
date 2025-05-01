package com.example.product.dto.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageListDTO {
    private Long productId;
    private List<ProductImageDTO> images = new ArrayList<>();
}
