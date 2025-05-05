package com.example.product.repository;

import com.example.product.model.Brand;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.product.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findBySlug(String slug);
    List<Brand> findByActiveOrderBySortOrderAscNameAsc(boolean active);
    List<Brand> findByPremiumOrderBySortOrderAscNameAsc(boolean premium);
    List<Brand> findByNameContainingIgnoreCaseOrderByNameAsc(String name);
    List<Brand> findAllByOrderBySortOrderAscNameAsc();

    // JPQL запрос для получения брендов по популярности в категории
    @Query("SELECT b, COUNT(p) as productCount FROM Brand b " +
            "JOIN b.products p " +
            "JOIN p.categories c " +
            "WHERE c.id = :categoryId AND p.status = 'ACTIVE' " +
            "GROUP BY b " +
            "ORDER BY productCount DESC")
    List<Object[]> findBrandsByPopularityInCategory(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT DISTINCT b FROM Brand b JOIN b.products p JOIN p.categories c WHERE c.id = :categoryId")
    List<Brand> findBrandsByCategoryId(@Param("categoryId") Long categoryId);

    // Другой вариант с подсчетом количества продуктов
    @Query("SELECT b, COUNT(p) FROM Brand b JOIN b.products p JOIN p.categories c WHERE c.id = :categoryId GROUP BY b ORDER BY COUNT(p) DESC")
    List<Object[]> findBrandsWithProductCountByCategoryId(@Param("categoryId") Long categoryId);


}