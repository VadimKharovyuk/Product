package com.example.product.repository;

import com.example.product.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.product.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
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
}