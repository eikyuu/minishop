package com.minishop.product.repository;

import com.minishop.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    // Spring Data génère les requêtes SQL automatiquement
}
