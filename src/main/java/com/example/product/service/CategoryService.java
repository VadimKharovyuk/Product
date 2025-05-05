package com.example.product.service;


import com.example.product.dto.Category.*;
import com.example.product.model.Category;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryService {

    /**
     * Создать новую категорию
     * @param categoryDto DTO с данными категории
     * @param image изображение категории (может быть null)
     * @return Детальная информация о созданной категории
     */
    CategoryDetailsDto createCategory(CategoryCreateDto categoryDto, MultipartFile image) throws IOException;

    /**
     * Обновить существующую категорию
     * @param id ID категории для обновления
     * @param categoryDto DTO с новыми данными
     * @param image новое изображение (может быть null, если изображение не меняется)
     * @return Детальная информация об обновленной категории
     */
    CategoryDetailsDto updateCategory(Long id, CategoryCreateDto categoryDto, MultipartFile image) throws IOException;

    /**
     * Получить категорию по ID с полной информацией
     * @param id ID категории
     * @return Детальное DTO категории
     */
    CategoryDetailsDto getCategoryById(Long id);

    /**
     * Получить все категории верхнего уровня (без родителя)
     * @return Список категорий верхнего уровня
     */
    List<CategoryListDto> getRootCategories();

    /**
     * Получить подкатегории для указанной родительской категории
     * @param parentId ID родительской категории
     * @return Список подкатегорий
     */
    List<CategoryListDto> getSubcategories(Long parentId);

    /**
     * Удалить категорию по ID
     * @param id ID категории для удаления
     */
    void deleteCategory(Long id);

    /**
     * Увеличить счетчик просмотров категории
     * @param id ID категории
     */
    void incrementViewCount(Long id);

    /**
     * Увеличить счетчик добавлений в корзину для категории
     * @param id ID категории
     */
    void incrementCartAddCount(Long id);

    /**
     * Увеличить счетчик заказов для категории
     * @param id ID категории
     * @param revenue сумма заказа
     */
    void incrementOrderCount(Long id, java.math.BigDecimal revenue);

    /**
     * Обновить флаг популярности категории
     * @param id ID категории
     * @param isPopular новое значение флага
     */
    void updatePopularStatus(Long id, boolean isPopular);

    /**
     * Поиск категорий по имени или описанию
     * @param query поисковый запрос
     * @return Список найденных категорий
     */
    List<CategoryListDto> searchCategories(String query);

    /**
     * Получить общее количество категорий
     * @return количество категорий
     */
    long getCategoriesCount();


    List<PopularCategoryDto> getPopularCategories();

    CategoryDetailsDto getCategoryBySlug(String slug);

    List<CategoryListDto> getCategoryBreadcrumbs(Long id);

    List<CategoryListDto> getCategoriesByLevel(int level);


    List<CategoryTreeDto> getCategoryTree();

    CategoryListDto getCategoryShortInfo(Long id);

}
