package com.minishop.product.mapper;

import com.minishop.product.dto.ProductDto;
import com.minishop.product.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

// componentModel = "spring" : MapStruct génère un @Component injectable par Spring
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    // MapStruct génère l'implémentation à la compilation
    // Il mappe champ par champ par correspondance de nom
    ProductDto toDto(ProductEntity entity);

    ProductEntity toEntity(ProductDto dto);
}