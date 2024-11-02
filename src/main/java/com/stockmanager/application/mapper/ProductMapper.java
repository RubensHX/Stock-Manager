package com.stockmanager.application.mapper;

import com.stockmanager.application.dto.ProductDTO;
import com.stockmanager.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ProductCategoryMapper.class})
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDTO toDTO(Product product);

    @Mapping(target = "stock", ignore = true)
    Product toModel(ProductDTO productDTO);
}
