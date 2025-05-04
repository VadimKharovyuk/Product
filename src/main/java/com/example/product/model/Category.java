package com.example.product.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    private Integer productCount;

    private String imageUrl;
    private String imageId;


    // Базовые счетчики популярности
    private Integer viewCount = 0; // Сколько раз просматривали категорию
    private Integer cartAddCount = 0; // Сколько раз товары из этой категории добавляли в корзину
    private Integer orderCount = 0; // Сколько раз товары из этой категории заказывали

    // Флаг популярности (можно устанавливать автоматически на основе счетчиков)
    private boolean isPopular = false;


    // Для расчета трендов и сезонности
    private Integer lastMonthOrderCount = 0; // Заказы за последний месяц
    private Integer lastWeekOrderCount = 0; // Заказы за последнюю неделю


    private String metaKeywords;
    private String metaTitle;



    // Общая сумма продаж по категории
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    // Дата последнего заказа товара из категории
    private LocalDateTime lastOrderDate;


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

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
