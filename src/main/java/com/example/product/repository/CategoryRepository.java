package com.example.product.repository;

import com.example.product.model.Category;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Найти категорию по слагу
     * @param slug URL-идентификатор категории
     * @return Категория
     */
    Optional<Category> findBySlug(String slug);

    /**
     * Найти все категории верхнего уровня (без родителя)
     * @param sort Параметры сортировки
     * @return Список категорий
     */
    List<Category> findByParentIsNull(Sort sort);

    /**
     * Найти подкатегории для указанной родительской категории
     * @param parentId ID родительской категории
     * @param sort Параметры сортировки
     * @return Список подкатегорий
     */
    List<Category> findByParentId(Long parentId, Sort sort);

    /**
     * Найти все активные категории
     * @param sort Параметры сортировки
     * @return Список активных категорий
     */
    List<Category> findByActiveTrue(Sort sort);

    /**
     * Найти все активные категории верхнего уровня
     * @param sort Параметры сортировки
     * @return Список активных категорий верхнего уровня
     */
    List<Category> findByParentIsNullAndActiveTrue(Sort sort);

    /**
     * Найти категории по имени или описанию
     * @param name Часть имени категории
     * @param description Часть описания категории
     * @param sort Параметры сортировки
     * @return Список категорий
     */
    List<Category> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String name, String description, Sort sort);

    /**
     * Получить популярные категории
     * @param pageable Параметры пагинации
     * @return Список популярных категорий
     */
    @Query("SELECT c FROM Category c WHERE c.isPopular = true AND c.active = true ORDER BY c.orderCount DESC")
    List<Category> findPopularCategories(org.springframework.data.domain.Pageable pageable);

    /**
     * Получить категории с наибольшим количеством заказов за последний месяц
     * @param pageable Параметры пагинации
     * @return Список категорий
     */
    @Query("SELECT c FROM Category c WHERE c.active = true ORDER BY c.lastMonthOrderCount DESC")
    List<Category> findTopCategoriesByLastMonthOrders(org.springframework.data.domain.Pageable pageable);

    /**
     * Проверить существует ли категория с указанным слагом
     * @param slug URL-идентификатор категории
     * @return true если категория существует
     */
    boolean existsBySlug(String slug);

    /**
     * Проверить существует ли категория с указанным слагом, исключая категорию с указанным ID
     * @param slug URL-идентификатор категории
     * @param id ID категории, которую нужно исключить из проверки
     * @return true если категория существует
     */
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.slug = :slug AND c.id != :id")
    boolean existsBySlugAndIdNot(@Param("slug") String slug, @Param("id") Long id);



    List<Category> findByIsPopularTrueAndActiveTrue();

    @Query("SELECT c FROM Category c WHERE c.active = true ORDER BY " +
            "(c.viewCount + c.cartAddCount * 3 + c.orderCount * 5) DESC")
    List<Category> findTopCategoriesByPopularityMetrics(Pageable pageable);

    default List<Category> findTopCategoriesByPopularityMetrics(int limit) {
        return findTopCategoriesByPopularityMetrics(PageRequest.of(0, limit));
    }
}