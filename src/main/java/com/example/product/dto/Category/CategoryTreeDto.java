package com.example.product.dto.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryTreeDto {
    private Long id;
    private String name;
    private String slug;
    private List<CategoryTreeDto> children;
}
