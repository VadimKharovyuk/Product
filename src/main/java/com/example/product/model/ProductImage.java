package com.example.product.model;

import com.example.product.enums.ImageType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

     @Enumerated(EnumType.STRING)
    private ImageType imageType = ImageType.GALLERY;


    @NotBlank
    private String imageUrl;

    private String imageId;

    private String alt;

    private Integer sortOrder;



    @CreatedDate
    private LocalDateTime uploadedAt;

}
