package com.stockmanager.application.dto;

public record MoveStockRequest(Long productId, Integer quantity, String movementType) {
}
