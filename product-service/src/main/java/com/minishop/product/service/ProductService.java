package com.minishop.product.service;

import com.minishop.product.dto.CreateProductRequest;
import com.minishop.product.dto.ProductDto;
import com.minishop.product.dto.UpdateProductRequest;
import com.minishop.product.entity.ProductEntity;
import com.minishop.product.exception.ProductAlreadyExistsException;
import com.minishop.product.exception.ProductNotFoundException;
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

    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    public List<ProductDto> findAll() {
        return productRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public ProductDto findById(Long id) {
        return productRepository.findById(id)
                .map(mapper::toDto)
                // orElseThrow : si absent → lève ProductNotFoundException
                // → intercepté par GlobalExceptionHandler → 404 RFC 7807
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional
    public ProductDto create(CreateProductRequest dto) {
        if (productRepository.existsByName(dto.name())) {
            throw new ProductAlreadyExistsException(dto.name());
        }

        ProductEntity product = mapper.toEntity(dto);
        ProductEntity saved = productRepository.save(product);

        return mapper.toDto(saved);
    }

    @Transactional
    public ProductDto update(Long id, UpdateProductRequest request) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        // Dirty checking : Hibernate détecte les changements sur l'entité
        // et génère l'UPDATE automatiquement au flush — pas besoin de save()
        if (request.name() != null) entity.setName(request.name());
        if (request.price() != null) entity.setPrice(request.price());

        // Pas de repository.save() nécessaire ici
        // Hibernate fait l'UPDATE en fin de transaction grâce au dirty checking
        return mapper.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }
}
