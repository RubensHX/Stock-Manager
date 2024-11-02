package com.stockmanager.application.mapper;

import com.stockmanager.application.dto.ProductCategoryDTO;
import com.stockmanager.domain.model.ProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductCategoryMapper {
    ProductCategoryMapper INSTANCE = Mappers.getMapper(ProductCategoryMapper.class);

    ProductCategoryDTO toDTO(ProductCategory productCategory);

    @Mapping(target = "product", ignore = true)
    ProductCategory toModel(ProductCategoryDTO productCategoryDTO);
}
