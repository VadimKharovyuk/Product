package com.example.product.service.serviceImpl;

import com.example.product.dto.Category.CategoryCreateDto;
import com.example.product.dto.Category.CategoryDetailsDto;
import com.example.product.dto.Category.CategoryListDto;
import com.example.product.dto.Category.PopularCategoryDto;
import com.example.product.maper.CategoryMapper;
import com.example.product.model.Category;
import com.example.product.repository.CategoryRepository;
import com.example.product.service.CategoryService;
import com.example.product.service.StorageService;
import com.example.product.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final StorageService storageService;


    @Override
    @Transactional
    public CategoryDetailsDto createCategory(CategoryCreateDto categoryDto, MultipartFile image) throws IOException {
        log.info("Создание новой категории: {}", categoryDto.getName());

        Category category = categoryMapper.toEntity(categoryDto);

        // Генерируем slug, если его нет
        if (category.getSlug() == null || category.getSlug().isEmpty()) {
            category.setSlug(SlugUtil.generateSlug(category.getName()));
        }

        // Устанавливаем родительскую категорию, если указана
        if (categoryDto.getParentId() != null) {
            Category parent = categoryRepository.findById(categoryDto.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Родительская категория не найдена: " + categoryDto.getParentId()));
            category.setParent(parent);
        }

        // Обрабатываем изображение, если оно есть
        if (image != null && !image.isEmpty()) {
            try {
                StorageService.StorageResult result = storageService.uploadImage(image);
                category.setImageUrl(result.getUrl());
                category.setImageId(result.getImageId());
                log.info("Изображение для категории успешно загружено: {}", result.getImageId());
            } catch (IOException e) {
                log.error("Ошибка при загрузке изображения для категории: {}", e.getMessage());
                throw e;
            }
        }

        LocalDateTime now = LocalDateTime.now();
        category.setCreatedAt(now);
        category.setUpdatedAt(now);

        // Сохраняем категорию
        Category savedCategory = categoryRepository.save(category);
        log.info("Категория успешно создана с ID: {}", savedCategory.getId());

        return categoryMapper.toDetailsDto(savedCategory);
    }

    @Override
    @Transactional
    public CategoryDetailsDto updateCategory(Long id, CategoryCreateDto categoryDto, MultipartFile image) throws IOException {
        log.info("Обновление категории с ID: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Категория не найдена: " + id));

        // Обновляем базовые поля
        categoryMapper.updateEntityFromDto(category, categoryDto);

        // Генерируем slug, если его нет
        if (category.getSlug() == null || category.getSlug().isEmpty()) {
            category.setSlug(SlugUtil.generateSlug(category.getName()));
        }

        // Обновляем родительскую категорию
        if (categoryDto.getParentId() != null) {
            // Проверяем, что родительская категория не является текущей категорией
            // или её дочерней категорией (предотвращение циклических связей)
            if (categoryDto.getParentId().equals(id)) {
                throw new IllegalArgumentException("Категория не может быть своим собственным родителем");
            }

            Category parent = categoryRepository.findById(categoryDto.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Родительская категория не найдена: " + categoryDto.getParentId()));

            // Проверка на циклические зависимости
            Category currentParent = parent;
            while (currentParent != null) {
                if (currentParent.getId().equals(id)) {
                    throw new IllegalArgumentException("Циклическая зависимость категорий не допускается");
                }
                currentParent = currentParent.getParent();
            }

            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        // Обрабатываем изображение, если оно есть
        if (image != null && !image.isEmpty()) {
            try {
                // Удаляем старое изображение, если оно есть
                if (category.getImageId() != null && !category.getImageId().isEmpty()) {
                    storageService.deleteImage(category.getImageId());
                    log.info("Старое изображение для категории удалено: {}", category.getImageId());
                }

                // Загружаем новое изображение
                StorageService.StorageResult result = storageService.uploadImage(image);
                category.setImageUrl(result.getUrl());
                category.setImageId(result.getImageId());
                log.info("Новое изображение для категории успешно загружено: {}", result.getImageId());
            } catch (IOException e) {
                log.error("Ошибка при загрузке изображения для категории: {}", e.getMessage());
                throw e;
            }
        }

        // Обновляем временную метку
        category.setUpdatedAt(LocalDateTime.now());

        // Сохраняем обновленную категорию
        Category updatedCategory = categoryRepository.save(category);
        log.info("Категория успешно обновлена с ID: {}", updatedCategory.getId());

        return categoryMapper.toDetailsDto(updatedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDetailsDto getCategoryById(Long id) {
        log.info("Получение категории по ID: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Категория не найдена: " + id));

        return categoryMapper.toDetailsDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryListDto> getRootCategories() {
        log.info("Получение корневых категорий");

        List<Category> rootCategories = categoryRepository.findByParentIsNull(
                Sort.by(Sort.Direction.ASC, "sortOrder", "name"));

        return categoryMapper.toListDto(rootCategories);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryListDto> getSubcategories(Long parentId) {
        log.info("Получение подкатегорий для родительской категории: {}", parentId);

        List<Category> subcategories = categoryRepository.findByParentId(parentId,
                Sort.by(Sort.Direction.ASC, "sortOrder", "name"));

        return categoryMapper.toListDto(subcategories);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        log.info("Удаление категории с ID: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Категория не найдена: " + id));

        // Проверяем наличие подкатегорий
        if (category.getSubcategories() != null && !category.getSubcategories().isEmpty()) {
            throw new IllegalStateException("Нельзя удалить категорию, имеющую подкатегории");
        }

        // Удаляем изображение, если оно есть
        if (category.getImageId() != null && !category.getImageId().isEmpty()) {
            boolean deleted = storageService.deleteImage(category.getImageId());
            if (deleted) {
                log.info("Изображение категории успешно удалено: {}", category.getImageId());
            } else {
                log.warn("Не удалось удалить изображение категории: {}", category.getImageId());
            }
        }

        // Удаляем категорию
        categoryRepository.delete(category);
        log.info("Категория успешно удалена: {}", id);
    }

    @Override
    @Transactional
    public void incrementViewCount(Long id) {
        log.debug("Увеличение счетчика просмотров для категории: {}", id);

        categoryRepository.findById(id).ifPresent(category -> {
            category.setViewCount(category.getViewCount() + 1);
            categoryRepository.save(category);
        });
    }

    @Override
    @Transactional
    public void incrementCartAddCount(Long id) {
        log.debug("Увеличение счетчика добавлений в корзину для категории: {}", id);

        categoryRepository.findById(id).ifPresent(category -> {
            category.setCartAddCount(category.getCartAddCount() + 1);
            categoryRepository.save(category);
        });
    }

    @Override
    @Transactional
    public void incrementOrderCount(Long id, BigDecimal revenue) {
        log.debug("Увеличение счетчика заказов для категории: {}", id);

        categoryRepository.findById(id).ifPresent(category -> {
            // Увеличиваем общий счетчик заказов
            category.setOrderCount(category.getOrderCount() + 1);

            // Увеличиваем счетчики недельных и месячных заказов
            // (Здесь должна быть более сложная логика для обновления недельных/месячных счетчиков)
            category.setLastWeekOrderCount(category.getLastWeekOrderCount() + 1);
            category.setLastMonthOrderCount(category.getLastMonthOrderCount() + 1);

            // Увеличиваем общую сумму дохода
            BigDecimal totalRevenue = category.getTotalRevenue().add(revenue);
            category.setTotalRevenue(totalRevenue);

            // Обновляем дату последнего заказа
            category.setLastOrderDate(LocalDateTime.now());

            categoryRepository.save(category);
        });
    }

    @Override
    @Transactional
    public void updatePopularStatus(Long id, boolean isPopular) {
        log.info("Обновление статуса популярности для категории {}: {}", id, isPopular);

        categoryRepository.findById(id).ifPresent(category -> {
            category.setPopular(isPopular);
            categoryRepository.save(category);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryListDto> searchCategories(String query) {
        log.info("Поиск категорий по запросу: {}", query);

        List<Category> foundCategories = categoryRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                query, query, Sort.by(Sort.Direction.ASC, "name"));

        return categoryMapper.toListDto(foundCategories);
    }

    @Override
    @Transactional(readOnly = true)
    public long getCategoriesCount() {
        return categoryRepository.count();
    }


    @Override
    public List<PopularCategoryDto> getPopularCategories() {
        // Найти популярные категории на основе флага isPopular
        List<Category> popularCategories = categoryRepository.findByIsPopularTrueAndActiveTrue();

        // Если список пустой, используем алгоритмический подход
        if (popularCategories.isEmpty()) {
            // Получаем топ-10 категорий по метрикам популярности
            popularCategories = categoryRepository.findTopCategoriesByPopularityMetrics(10);
        }

        return popularCategories.stream()
                .filter(Objects::nonNull)
                .map(categoryMapper::toPopularCategoryDto)
                .collect(Collectors.toList());

    }
}