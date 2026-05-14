package com.minishop.product.service;

import com.minishop.product.dto.CreateProductRequest;
import com.minishop.product.dto.ProductDto;
import com.minishop.product.entity.ProductEntity;
import com.minishop.product.mapper.ProductMapper;
import com.minishop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductService {
    private final ProductMapper productMapper;

    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    // readOnly=true : optimisation — pas de dirty checking Hibernate
    @Transactional(readOnly = true)
    public List<ProductDto> findAll() {
        log.info("findAll products");
        return productRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public ProductDto findById(Long id) {
        return productRepository.findById(id).map(productMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
    }

    @Transactional
    public ProductDto create(CreateProductRequest request) {
        ProductEntity entity = ProductEntity.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .stock(request.stock())
                .build();
        ProductEntity saved = productRepository.save(entity);
        log.info("Product created with id={}", saved.getId());
        return mapper.toDto(saved);
    }
}
