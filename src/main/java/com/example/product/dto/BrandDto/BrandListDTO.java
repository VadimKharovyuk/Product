package com.example.product.dto.BrandDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandListDTO {
    private Long id;
    private String name;
    private String slug;
    private String bannerUrl;
    private boolean active;
    private boolean premium;
    private String country;
    private Integer productCount;
    private Integer sortOrder;
}