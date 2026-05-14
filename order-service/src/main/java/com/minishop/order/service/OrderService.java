package com.minishop.order.service;

import com.minishop.order.client.ProductClient;
import com.minishop.order.dto.CreateOrderRequest;
import com.minishop.order.dto.OrderDto;
import com.minishop.order.dto.ProductDto;
import com.minishop.order.entity.OrderEntity;
import com.minishop.order.entity.OrderStatus;
import com.minishop.order.mapper.OrderMapper;
import com.minishop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final ProductClient productClient;

    @Transactional
    public OrderDto create(CreateOrderRequest request) {
        ProductDto product = productClient.findById(request.productId());

        // Snapshot du prix au moment de la commande pour figer la valeur facturée.
        BigDecimal unitPrice = product.price();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(request.quantity()));

        OrderEntity order = OrderEntity.builder()
                .productId(request.productId())
                .quantity(request.quantity())
                .unitPrice(unitPrice)
                .totalPrice(totalPrice)
                .createdAt(Instant.now())
                .status(OrderStatus.PENDING)
                .build();

        OrderEntity saved = repository.save(order);
        log.info("Order created id={} productId={} total={}",
                saved.getId(), saved.getProductId(), saved.getTotalPrice());

        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }
}
