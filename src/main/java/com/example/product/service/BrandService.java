package com.example.product.service;

import com.example.product.dto.BrandDto.BrandCreateRequest;
import com.example.product.dto.BrandDto.BrandDTO;
import com.example.product.dto.BrandDto.BrandListDTO;
import com.example.product.dto.BrandDto.BrandUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BrandService {
    // CRUD операции
    BrandDTO createBrand(BrandCreateRequest request) throws IOException;
    BrandDTO updateBrand(Long id, BrandUpdateRequest request) throws IOException;
    void deleteBrand(Long id);
    Optional<BrandDTO> getBrandById(Long id);
    Optional<BrandDTO> getBrandBySlug(String slug);

    // Операции со списками
    List<BrandListDTO> getAllBrands();
    Page<BrandListDTO> getBrandsPaginated(Pageable pageable);
    List<BrandListDTO> getBrandsByActive(boolean active);
    List<BrandListDTO> getBrandsByPremium(boolean premium);
    List<BrandListDTO> searchBrands(String query);

    // Операции с баннером
    BrandDTO uploadBannerImage(Long brandId, MultipartFile file) throws IOException;
    void deleteBannerImage(Long brandId) throws IOException;

    // Статистика
    void updateProductCount(Long brandId);
    void incrementProductCount(Long brandId);
    void decrementProductCount(Long brandId);
}