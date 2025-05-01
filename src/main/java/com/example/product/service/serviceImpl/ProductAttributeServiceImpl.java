package com.example.product.service.serviceImpl;

import com.example.product.Exception.ResourceNotFoundException;
import com.example.product.dto.ProductAttribute.ProductAttributeCreateDTO;
import com.example.product.dto.ProductAttribute.ProductAttributeDTO;
import com.example.product.dto.ProductAttribute.ProductAttributeListDTO;
import com.example.product.dto.ProductAttribute.ProductAttributeUpdateDTO;
import com.example.product.maper.ProductAttributeMapper;
import com.example.product.model.Product;
import com.example.product.model.ProductAttribute;

import com.example.product.repository.ProductAttributeRepository;
import com.example.product.repository.ProductRepository;
import com.example.product.service.ProductAttributeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductAttributeServiceImpl implements ProductAttributeService {

    private final ProductAttributeRepository productAttributeRepository;
    private final ProductRepository productRepository;
    private final ProductAttributeMapper productAttributeMapper;

    @Override
    public ProductAttributeListDTO getProductAttributes(Long productId) {
        // Проверяем существование продукта
        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Продукт не найден с ID: " + productId));

        List<ProductAttribute> attributes = productAttributeRepository.findByProductId(productId);
        return productAttributeMapper.toProductAttributeListDTO(productId, attributes);
    }

    @Override
    public ProductAttributeDTO getAttributeById(Long attributeId) {
        ProductAttribute attribute = findAttributeById(attributeId);
        return productAttributeMapper.toDTO(attribute);
    }

    @Override
    @Transactional
    public ProductAttributeDTO createAttribute(ProductAttributeCreateDTO createDTO) {
        // Получаем продукт
        Product product = productRepository.findById(createDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Продукт не найден с ID: " + createDTO.getProductId()));

        // Создаем атрибут
        ProductAttribute attribute = productAttributeMapper.toEntity(createDTO, product);
        attribute = productAttributeRepository.save(attribute);

        log.info("Создан новый атрибут с ID: {} для продукта: {}", attribute.getId(), product.getId());

        return productAttributeMapper.toDTO(attribute);
    }

    @Override
    @Transactional
    public ProductAttributeDTO updateAttribute(Long attributeId, ProductAttributeUpdateDTO updateDTO) {
        // Находим атрибут
        ProductAttribute attribute = findAttributeById(attributeId);

        // Обновляем атрибут
        productAttributeMapper.updateEntityFromDTO(updateDTO, attribute);
        attribute = productAttributeRepository.save(attribute);

        log.info("Обновлен атрибут с ID: {} для продукта: {}", attribute.getId(), attribute.getProduct().getId());

        return productAttributeMapper.toDTO(attribute);
    }

    @Override
    @Transactional
    public void deleteAttribute(Long attributeId) {
        // Находим атрибут
        ProductAttribute attribute = findAttributeById(attributeId);

        // Удаляем атрибут
        productAttributeRepository.delete(attribute);

        log.info("Удален атрибут с ID: {} для продукта: {}", attribute.getId(), attribute.getProduct().getId());
    }

    @Override
    @Transactional
    public List<ProductAttributeDTO> createAttributes(Long productId, List<ProductAttributeCreateDTO> createDTOs) {
        // Получаем продукт
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Продукт не найден с ID: " + productId));

        List<ProductAttribute> attributes = new ArrayList<>();

        // Создаем атрибуты
        for (ProductAttributeCreateDTO createDTO : createDTOs) {
            ProductAttribute attribute = productAttributeMapper.toEntity(createDTO, product);
            attributes.add(attribute);
        }

        // Сохраняем атрибуты
        attributes = productAttributeRepository.saveAll(attributes);

        log.info("Создано {} атрибутов для продукта: {}", attributes.size(), productId);

        return productAttributeMapper.toDTOList(attributes);
    }

    @Override
    @Transactional
    public void deleteAllProductAttributes(Long productId) {
        // Проверяем существование продукта
        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Продукт не найден с ID: " + productId));

        // Удаляем атрибуты
        int count = productAttributeRepository.deleteByProductId(productId);

        log.info("Удалено {} атрибутов для продукта: {}", count, productId);
    }

    @Override
    public List<ProductAttributeDTO> findAttributesByColor(String color) {
        List<ProductAttribute> attributes = productAttributeRepository.findByColorIgnoreCase(color);
        return productAttributeMapper.toDTOList(attributes);
    }

    @Override
    public List<ProductAttributeDTO> findAttributesBySize(String size) {
        List<ProductAttribute> attributes = productAttributeRepository.findBySizeIgnoreCase(size);
        return productAttributeMapper.toDTOList(attributes);
    }

    @Override
    public List<ProductAttributeDTO> findAttributesByMaterial(String material) {
        List<ProductAttribute> attributes = productAttributeRepository.findByMaterialIgnoreCase(material);
        return productAttributeMapper.toDTOList(attributes);
    }

    /**
     * Вспомогательный метод для поиска атрибута по ID
     */
    private ProductAttribute findAttributeById(Long attributeId) {
        return productAttributeRepository.findById(attributeId)
                .orElseThrow(() -> new ResourceNotFoundException("Атрибут не найден с ID: " + attributeId));
    }
}