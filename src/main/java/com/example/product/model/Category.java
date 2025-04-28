package com.example.product.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    private String imageUrl;
    private String imageId;

    @Column(unique = true)
    private String slug; // URL-friendly имя (например, "electronics")

    // Самоссылающаяся связь для построения иерархии
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> subcategories = new ArrayList<>();

    // Порядок сортировки категории
    private Integer sortOrder;

    private boolean active = true;
}
