package com.example.product.service.serviceImpl;

import com.example.product.dto.BrandDto.BrandCreateRequest;
import com.example.product.dto.BrandDto.BrandDTO;
import com.example.product.dto.BrandDto.BrandListDTO;
import com.example.product.dto.BrandDto.BrandUpdateRequest;
import com.example.product.maper.BrandMapper;
import com.example.product.model.Brand;
import com.example.product.repository.BrandRepository;
import com.example.product.repository.ProductRepository;
import com.example.product.service.BrandService;
import com.example.product.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final BrandMapper brandMapper;
    private final StorageService storageService;

    @Override
    @Transactional
    public BrandDTO createBrand(BrandCreateRequest request) throws IOException {
        Brand brand = brandMapper.toEntity(request);

        // Загрузка изображения баннера, если оно предоставлено
        if (request.getBanner() != null && !request.getBanner().isEmpty()) {
            uploadBannerImageInternal(brand, request.getBanner());
        }

        brand = brandRepository.save(brand);
        log.info("Создан новый бренд: {}", brand.getName());
        return brandMapper.toDTO(brand);
    }

    @Override
    @Transactional
    public BrandDTO updateBrand(Long id, BrandUpdateRequest request) throws IOException {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Бренд с ID " + id + " не найден"));

        brandMapper.updateEntityFromRequest(brand, request);

        // Загрузка изображения баннера, если оно предоставлено
        if (request.getBanner() != null && !request.getBanner().isEmpty()) {
            // Если есть старый баннер, удаляем его
            if (StringUtils.hasText(brand.getBannerImageId())) {
                deleteBannerImageInternal(brand);
            }
            uploadBannerImageInternal(brand, request.getBanner());
        }

        brand = brandRepository.save(brand);
        log.info("Обновлен бренд: {}", brand.getName());
        return brandMapper.toDTO(brand);
    }

    @Override
    @Transactional
    public void deleteBrand(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Бренд с ID " + id + " не найден"));

        // Проверим, есть ли продукты у этого бренда
        long productCount = productRepository.countByBrandId(id);
        if (productCount > 0) {
            throw new RuntimeException("Невозможно удалить бренд, так как с ним связано " + productCount + " продуктов");
        }

        // Удаляем баннер, если он есть
        if (StringUtils.hasText(brand.getBannerImageId())) {
            deleteBannerImageInternal(brand);
        }

        brandRepository.delete(brand);
        log.info("Удален бренд: {}", brand.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BrandDTO> getBrandById(Long id) {
        return brandRepository.findById(id)
                .map(brandMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BrandDTO> getBrandBySlug(String slug) {
        return brandRepository.findBySlug(slug)
                .map(brandMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandListDTO> getAllBrands() {
        return brandMapper.toListDTO(brandRepository.findAllByOrderBySortOrderAscNameAsc());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BrandListDTO> getBrandsPaginated(Pageable pageable) {
        return brandRepository.findAll(pageable)
                .map(brandMapper::toListDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandListDTO> getBrandsByActive(boolean active) {
        return brandMapper.toListDTO(brandRepository.findByActiveOrderBySortOrderAscNameAsc(active));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandListDTO> getBrandsByPremium(boolean premium) {
        return brandMapper.toListDTO(brandRepository.findByPremiumOrderBySortOrderAscNameAsc(premium));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandListDTO> searchBrands(String query) {
        if (!StringUtils.hasText(query)) {
            return getAllBrands();
        }
        return brandMapper.toListDTO(brandRepository.findByNameContainingIgnoreCaseOrderByNameAsc(query));
    }

    @Override
    @Transactional
    public BrandDTO uploadBannerImage(Long brandId, MultipartFile file) throws IOException {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("Бренд с ID " + brandId + " не найден"));

        // Если есть старый баннер, удаляем его
        if (StringUtils.hasText(brand.getBannerImageId())) {
            deleteBannerImageInternal(brand);
        }

        uploadBannerImageInternal(brand, file);
        brand = brandRepository.save(brand);
        log.info("Загружен баннер для бренда: {}", brand.getName());
        return brandMapper.toDTO(brand);
    }

    @Override
    @Transactional
    public void deleteBannerImage(Long brandId) throws IOException {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("Бренд с ID " + brandId + " не найден"));

        if (!StringUtils.hasText(brand.getBannerImageId())) {
            return; // Нечего удалять
        }

        deleteBannerImageInternal(brand);
        brand.setBannerUrl(null);
        brand.setBannerImageId(null);
        brandRepository.save(brand);
        log.info("Удален баннер для бренда: {}", brand.getName());
    }

    @Override
    @Transactional
    public void updateProductCount(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("Бренд с ID " + brandId + " не найден"));

        long count = productRepository.countByBrandId(brandId);
        brand.setProductCount((int) count);
        brandRepository.save(brand);
        log.info("Обновлено количество продуктов для бренда {}: {}", brand.getName(), count);
    }

    @Override
    @Transactional
    public void incrementProductCount(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("Бренд с ID " + brandId + " не найден"));

        int newCount = brand.getProductCount() != null ? brand.getProductCount() + 1 : 1;
        brand.setProductCount(newCount);
        brandRepository.save(brand);
    }

    @Override
    @Transactional
    public void decrementProductCount(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("Бренд с ID " + brandId + " не найден"));

        if (brand.getProductCount() != null && brand.getProductCount() > 0) {
            brand.setProductCount(brand.getProductCount() - 1);
            brandRepository.save(brand);
        }
    }


    @Override
    public long getBrandCount() {
        return brandRepository.count();
    }

    // Внутренние вспомогательные методы для работы с изображениями

    private void uploadBannerImageInternal(Brand brand, MultipartFile file) throws IOException {
        try {
            StorageService.StorageResult result = storageService.uploadImage(file);
            brand.setBannerUrl(result.getUrl());
            brand.setBannerImageId(result.getImageId());
            log.debug("Загружен баннер для бренда. URL: {}, ID: {}", result.getUrl(), result.getImageId());
        } catch (IOException e) {
            log.error("Ошибка при загрузке баннера для бренда {}: {}", brand.getName(), e.getMessage());
            throw e;
        }
    }

    private void deleteBannerImageInternal(Brand brand) {
        if (StringUtils.hasText(brand.getBannerImageId())) {
            boolean deleted = storageService.deleteImage(brand.getBannerImageId());
            if (deleted) {
                log.debug("Удален баннер для бренда {}. ID: {}", brand.getName(), brand.getBannerImageId());
            } else {
                log.warn("Не удалось удалить баннер для бренда {}. ID: {}", brand.getName(), brand.getBannerImageId());
            }
        }
    }
}