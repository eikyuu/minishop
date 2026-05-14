package com.minishop.order.mapper;

import com.minishop.order.dto.CreateOrderRequest;
import com.minishop.order.dto.OrderDto;
import com.minishop.order.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    OrderDto toDto(OrderEntity entity);

    OrderEntity toEntity(CreateOrderRequest dto);

}