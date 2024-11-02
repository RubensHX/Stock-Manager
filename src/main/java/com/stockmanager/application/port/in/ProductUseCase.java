package com.stockmanager.application.port.in;

import com.stockmanager.application.dto.ProductDTO;

import java.util.List;

public interface ProductUseCase {
    ProductDTO getProduct(Long productId);

    List<ProductDTO> getProducts();

    void updateProduct(ProductDTO dto);

}
