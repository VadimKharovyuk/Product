package com.example.product.service.serviceImpl;

import com.example.product.Exception.ResourceNotFoundException;
import com.example.product.dto.ProductImage.ProductImageCreateDTO;
import com.example.product.dto.ProductImage.ProductImageDTO;
import com.example.product.dto.ProductImage.ProductImageListDTO;
import com.example.product.dto.ProductImage.ProductImageUpdateDTO;
import com.example.product.enums.ImageType;

import com.example.product.maper.ProductImageMapper;
import com.example.product.model.Product;
import com.example.product.model.ProductImage;

import com.example.product.repository.ProductImageRepository;
import com.example.product.repository.ProductRepository;
import com.example.product.service.ProductImageService;
import com.example.product.service.StorageService;
import com.example.product.service.StorageService.StorageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final ProductImageMapper productImageMapper;
    private final StorageService storageService;

    @Override
    public ProductImageListDTO getProductImages(Long productId) {
        List<ProductImage> images = productImageRepository.findByProductIdOrderBySortOrderAsc(productId);
        return productImageMapper.toProductImageListDTO(productId, images);
    }

    @Override
    public ProductImageDTO getImageById(Long imageId) {
        ProductImage image = findImageById(imageId);
        return productImageMapper.toDTO(image);
    }

    @Override
    @Transactional
    public ProductImageDTO createProductImage(ProductImageCreateDTO createDTO) throws IOException {
        Product product = productRepository.findById(createDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Продукт не найден с ID: " + createDTO.getProductId()));

        MultipartFile imageFile = createDTO.getImageFile();
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Файл изображения не может быть пустым");
        }

        // Загрузка изображения в облачное хранилище
        StorageResult result = storageService.uploadImage(imageFile);

        // Определение порядка сортировки
        Integer sortOrder = createDTO.getSortOrder();
        if (sortOrder == null) {
            // Если порядок не указан, ставим в конец
            Integer maxSortOrder = productImageRepository.findMaxSortOrderByProductId(product.getId());
            sortOrder = (maxSortOrder == null) ? 0 : maxSortOrder + 1;
        }

        // Создание записи в БД
        ProductImage image = productImageMapper.toEntity(
                createDTO,
                product,
                result.getUrl(),
                result.getImageId()
        );
        image.setSortOrder(sortOrder);

        image = productImageRepository.save(image);
        log.info("Создано новое изображение с ID: {} для продукта: {}", image.getId(), product.getId());

        return productImageMapper.toDTO(image);
    }

    @Override
    @Transactional
    public ProductImageDTO updateProductImage(Long imageId, ProductImageUpdateDTO updateDTO) throws IOException {
        ProductImage image = findImageById(imageId);

        // Обработка нового файла, если он есть
        if (updateDTO.getImageFile() != null && !updateDTO.getImageFile().isEmpty()) {
            // Сначала удаляем старое изображение
            if (image.getImageId() != null && !image.getImageId().isEmpty()) {
                storageService.deleteImage(image.getImageId());
            }

            // Загружаем новое изображение
            StorageResult result = storageService.uploadImage(updateDTO.getImageFile());

            // Обновляем URL и ID
            image.setImageUrl(result.getUrl());
            image.setImageId(result.getImageId());
        }

        // Обновляем остальные поля
        productImageMapper.updateEntityFromDTO(updateDTO, image);
        image = productImageRepository.save(image);

        log.info("Обновлено изображение с ID: {} для продукта: {}", image.getId(), image.getProduct().getId());

        return productImageMapper.toDTO(image);
    }

    @Override
    @Transactional
    public boolean deleteProductImage(Long imageId) {
        ProductImage image = findImageById(imageId);

        // Удаление из облачного хранилища
        boolean storageDeleteSuccess = true;
        if (image.getImageId() != null && !image.getImageId().isEmpty()) {
            storageDeleteSuccess = storageService.deleteImage(image.getImageId());
        }

        // Удаление записи из БД
        productImageRepository.delete(image);
        log.info("Удалено изображение с ID: {} для продукта: {}", image.getId(), image.getProduct().getId());

        return storageDeleteSuccess;
    }

    @Override
    @Transactional
    public void updateImageOrder(Long productId, List<Long> imageIds) {
        if (imageIds == null || imageIds.isEmpty()) {
            return;
        }

        // Проверяем существование продукта
        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Продукт не найден с ID: " + productId));

        // Получаем все изображения продукта
        List<ProductImage> images = productImageRepository.findByProductId(productId);

        // Проверяем, что все ID из входящего списка принадлежат данному продукту
        List<Long> existingIds = images.stream()
                .map(ProductImage::getId)
                .collect(Collectors.toList());

        for (Long id : imageIds) {
            if (!existingIds.contains(id)) {
                throw new ResourceNotFoundException("Изображение с ID: " + id +
                        " не принадлежит продукту с ID: " + productId);
            }
        }

        // Обновляем порядок сортировки
        for (int i = 0; i < imageIds.size(); i++) {
            productImageRepository.updateSortOrder(imageIds.get(i), i);
        }

        log.info("Обновлен порядок сортировки изображений для продукта: {}", productId);
    }

    @Override
    @Transactional
    public ProductImageDTO setMainProductImage(Long imageId) {
        ProductImage image = findImageById(imageId);
        Long productId = image.getProduct().getId();

        // Сбрасываем главное изображение у всех изображений продукта
        productImageRepository.resetMainImageByProductId(productId);

        // Устанавливаем новое главное изображение
        image.setImageType(ImageType.MAIN);
        image = productImageRepository.save(image);

        log.info("Установлено главное изображение с ID: {} для продукта: {}", imageId, productId);

        return productImageMapper.toDTO(image);
    }

    /**
     * Вспомогательный метод для поиска изображения по ID
     */
    private ProductImage findImageById(Long imageId) {
        return productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Изображение не найдено с ID: " + imageId));
    }
}