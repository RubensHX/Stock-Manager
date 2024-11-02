package com.stockmanager.application.port.in;

import com.stockmanager.application.dto.ProductDTO;
import com.stockmanager.domain.enums.MovementType;
import com.stockmanager.domain.model.Stock;

public interface StockUseCase {
    void addProduct(ProductDTO productDTO);

    void moveStock(Long productId, Integer quantity, MovementType movementType);

    Integer getAvailableQuantity(Long productId);

    void validateStockMovement(Stock stock, MovementType movementType);
}
