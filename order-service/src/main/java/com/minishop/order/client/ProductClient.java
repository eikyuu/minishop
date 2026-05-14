package com.minishop.order.client;

import com.minishop.order.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class ProductClient {

    private final RestTemplate restTemplate;
    private final String productServiceUrl;

    // @Value injecte la valeur depuis application.properties
    // Jamais l'URL hardcodée dans le code
    public ProductClient(
            RestTemplate restTemplate,
            @Value("${product.service.url}") String productServiceUrl) {
        this.restTemplate = restTemplate;
        this.productServiceUrl = productServiceUrl;
    }

    public ProductDto findById(Long productId) {
        String url = productServiceUrl + "/api/products/" + productId;
        log.info("Calling product-service GET {}", url);

        try {
            return restTemplate.getForObject(url, ProductDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Product {} not found in product-service", productId);
            throw new ProductNotFoundException(productId);
        }
    }

    // Exception locale à order-service
    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(Long id) {
            super("Product not found: " + id);
        }
    }
}