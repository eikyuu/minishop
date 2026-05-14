package com.minishop.order.service;

import com.minishop.order.dto.CreateOrderRequest;
import com.minishop.order.dto.OrderDto;
import com.minishop.order.entity.OrderEntity;
import com.minishop.order.entity.OrderStatus;
import com.minishop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    public List<OrderDto> findAll() {
        return orderRepository.findAll().stream()
                .map(OrderDto::fromEntity)
                .toList();
    }

    public OrderDto findById(Long id) {
        return orderRepository.findById(id)
                .map(OrderDto::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
    }

    @Transactional
    public OrderDto create(CreateOrderRequest request) {
        OrderEntity entity = OrderEntity.builder()
                .productId(request.productId())
                .quantity(request.quantity())
                .status(OrderStatus.PENDING)
                .createdAt(Instant.now())
                .build();
        OrderEntity saved = orderRepository.save(entity);
        log.info("Order created with id={} for productId={}", saved.getId(), saved.getProductId());
        return OrderDto.fromEntity(saved);
    }
}
