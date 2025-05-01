package com.example.product.service.serviceImpl;

import com.example.product.dto.BrandDto.BrandDTO;
import com.example.product.dto.Product.ProductCreateRequest;
import com.example.product.dto.Product.ProductDTO;
import com.example.product.dto.Product.ProductListDTO;
import com.example.product.dto.Product.ProductSearchResponse;
import com.example.product.dto.Product.ProductUpdateRequest;
import com.example.product.enums.ImageType;
import com.example.product.enums.ProductStatus;
import com.example.product.maper.ProductMapper;
import com.example.product.model.Brand;
import com.example.product.model.Category;
import com.example.product.model.Product;
import com.example.product.model.ProductImage;
import com.example.product.repository.BrandRepository;
import com.example.product.repository.CategoryRepository;
import com.example.product.repository.ProductRepository;
import com.example.product.service.BrandService;
import com.example.product.service.ProductImageService;
import com.example.product.service.ProductService;

import com.example.product.util.SlugUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductMapper productMapper;
    private final SlugUtil slugGenerator;
    private final BrandService brandService;


    @Override
    @Transactional
    public ProductDTO createProduct(ProductCreateRequest request) throws IOException {
        Product product = productMapper.toEntity(request);

        // Генерация slug
        product.setSlug(slugGenerator.generateSlug(request.getName()));

        // Установка времени создания и обновления
        LocalDateTime now = LocalDateTime.now();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);

        // Добавление категорий
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            Set<Category> categories = request.getCategoryIds().stream()
                    .map(categoryId -> categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new EntityNotFoundException("Категория не найдена: " + categoryId)))
                    .collect(Collectors.toSet());
            product.setCategories(categories);
        }

        // Установка бренда
        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new EntityNotFoundException("Бренд не найден: " + request.getBrandId()));
            product.setBrand(brand);
        }

        // Сохранение продукта
        Product savedProduct = productRepository.save(product);

        // Обновление счетчика продуктов в бренде, если бренд установлен
        if (product.getBrand() != null) {
             brandService.incrementProductCount(product.getBrand().getId());
        }

        return productMapper.toDTO(savedProduct);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductUpdateRequest request) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Продукт не найден: " + id));

        // Обновление полей продукта
        productMapper.updateProductFromDTO(product, request);

        // Обновление времени изменения
        product.setUpdatedAt(LocalDateTime.now());

        // Обновление категорий, если они указаны
        if (request.getCategoryIds() != null) {
            Set<Category> categories = request.getCategoryIds().stream()
                    .map(categoryId -> categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new EntityNotFoundException("Категория не найдена: " + categoryId)))
                    .collect(Collectors.toSet());
            product.setCategories(categories);
        }

        // Обновление бренда, если он указан
        if (request.getBrandId() != null) {
            Long oldBrandId = product.getBrand() != null ? product.getBrand().getId() : null;

            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new EntityNotFoundException("Бренд не найден: " + request.getBrandId()));
            product.setBrand(brand);

            // Обновление счетчиков продуктов у брендов
            if (oldBrandId != null && !oldBrandId.equals(request.getBrandId())) {
                // Предполагается, что у вас есть BrandService с методами incrementProductCount и decrementProductCount
                // brandService.decrementProductCount(oldBrandId);
                // brandService.incrementProductCount(request.getBrandId());
            }
        }

        // Сохранение продукта
        Product updatedProduct = productRepository.save(product);
        return productMapper.toDTO(updatedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> getProductBySlug(String slug) {
        return productRepository.findBySlug(slug)
                .map(productMapper::toDTO);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Продукт не найден: " + id));

        // Уменьшение счетчика продуктов в бренде, если бренд установлен
        if (product.getBrand() != null) {
            // Предполагается, что у вас есть BrandService с методом decrementProductCount
            // brandService.decrementProductCount(product.getBrand().getId());
        }

        productRepository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductListDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toListDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductListDTO> getProductsPaginated(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toListDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductListDTO> getProductsByBrand(Long brandId) {
        return productRepository.findByBrandId(brandId).stream()
                .map(productMapper::toListDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductListDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoriesId(categoryId).stream()
                .map(productMapper::toListDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductListDTO> getProductsByStatus(ProductStatus status) {
        return productRepository.findByStatus(status).stream()
                .map(productMapper::toListDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductListDTO> getFeaturedProducts() {
        return productRepository.findByFeaturedTrue().stream()
                .map(productMapper::toListDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductSearchResponse searchProducts(String query, Pageable pageable) {
        Page<Product> productPage = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                query, query, pageable);

        List<ProductListDTO> products = productPage.getContent().stream()
                .map(productMapper::toListDTO)
                .collect(Collectors.toList());

        return ProductSearchResponse.builder()
                .products(products)
                .total(productPage.getTotalElements())
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductSearchResponse searchProductsByPrice(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Page<Product> productPage = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);

        List<ProductListDTO> products = productPage.getContent().stream()
                .map(productMapper::toListDTO)
                .collect(Collectors.toList());

        return ProductSearchResponse.builder()
                .products(products)
                .total(productPage.getTotalElements())
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .build();
    }

    @Override
    @Transactional
    public ProductDTO addProductImage(Long productId, MultipartFile image, boolean isMain) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Продукт не найден: " + productId));

        // Используем сервис изображений для создания нового изображения
        // Предполагается, что у вас есть подходящие методы в ProductImageService
        // и реализован класс ProductImageCreateDTO

        // Пример реализации:
        /*
        ProductImageCreateDTO createDTO = new ProductImageCreateDTO();
        createDTO.setProductId(productId);
        createDTO.setImageType(isMain ? ImageType.MAIN : ImageType.GALLERY);

        // Если это главное изображение, изменяем тип всех существующих главных изображений
        if (isMain) {
            product.getImages().stream()
                    .filter(img -> img.getImageType() == ImageType.MAIN)
                    .forEach(img -> img.setImageType(ImageType.GALLERY));
        }

        productImageService.createProductImage(createDTO);
        */

        // Перезагружаем продукт, чтобы получить обновленный список изображений
        Product updatedProduct = productRepository.findById(productId).orElseThrow();
        return productMapper.toDTO(updatedProduct);
    }

    @Override
    @Transactional
    public ProductDTO updateProductCategories(Long productId, List<Long> categoryIds) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Продукт не найден: " + productId));

        Set<Category> categories = categoryIds.stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new EntityNotFoundException("Категория не найдена: " + categoryId)))
                .collect(Collectors.toSet());

        product.setCategories(categories);
        product.setUpdatedAt(LocalDateTime.now());

        Product updatedProduct = productRepository.save(product);
        return productMapper.toDTO(updatedProduct);
    }

    @Override
    @Transactional
    public void incrementViewCount(Long productId) {
        // Предполагается, что у вас есть поле viewCount в модели Product
        // и соответствующий метод в репозитории

        // productRepository.incrementViewCount(productId);
        // или
        /*
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Продукт не найден: " + productId));
        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);
        */
    }

    @Override
    @Transactional
    public void updateStockQuantity(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Продукт не найден: " + productId));

        product.setStockQuantity(quantity);
        product.setUpdatedAt(LocalDateTime.now());

        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public long getProductCount() {
        return productRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getProductCountByStatus(ProductStatus status) {
        return productRepository.countByStatus(status);
    }
}