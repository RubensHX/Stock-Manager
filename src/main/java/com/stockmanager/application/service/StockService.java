package com.stockmanager.application.service;

import com.stockmanager.application.dto.ProductDTO;
import com.stockmanager.application.mapper.ProductMapper;
import com.stockmanager.application.port.in.ProductUseCase;
import com.stockmanager.application.port.in.StockUseCase;
import com.stockmanager.application.port.out.ProductRepository;
import com.stockmanager.application.port.out.StockRepository;
import com.stockmanager.domain.enums.MovementType;
import com.stockmanager.domain.model.Product;
import com.stockmanager.domain.model.Stock;
import com.stockmanager.domain.model.StockMovement;
import com.stockmanager.infrastructure.advice.NotFoundException;
import com.stockmanager.infrastructure.advice.UnavailableMovement;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService implements ProductUseCase, StockUseCase {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    @Override
    public ProductDTO getProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException(String.format("Product %d not found", productId)));
        return ProductMapper.INSTANCE.toDTO(product);
    }

    @Override
    public void updateProduct(@NotNull ProductDTO dto) {
        Product product = ProductMapper.INSTANCE.toModel(dto);
        productRepository.save(product);
    }

    @Override
    public List<ProductDTO> getProducts() {
        return productRepository.findAll().stream().map(ProductMapper.INSTANCE::toDTO).toList();
    }

    @Override
    public void addProduct(ProductDTO productDTO) {
        Product product = ProductMapper.INSTANCE.toModel(productDTO);

        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setAvailableQuantity(0);
        stock.setMinimumQuantity(0);

        product.setStock(stock);

        productRepository.save(product);
    }

    @Override
    public void moveStock(Long productId, Integer quantity, MovementType movementType) {
        Stock stock = stockRepository.findByProductId(productId).orElseThrow(() -> new NotFoundException(String.format("Stock not found for product %d", productId)));

        validateStockMovement(productId, movementType);

        Integer newQuantity = movementType.equals(MovementType.IN)
                ? stock.getAvailableQuantity() + quantity
                : stock.getAvailableQuantity() - quantity;

        stock.setAvailableQuantity(newQuantity);

        StockMovement stockMovement = new StockMovement(stock, quantity, movementType);
        stock.getStockMovements().add(stockMovement);

        stockRepository.save(stock);
    }

    @Override
    public Integer getAvailableQuantity(Long productId) {
        return stockRepository.findByProductId(productId)
                .map(Stock::getAvailableQuantity)
                .orElseThrow(() -> new NotFoundException(String.format("Stock not found for product %d", productId)));
    }

    @Override
    public void validateStockMovement(Long productId, MovementType movementType) {
        if (getAvailableQuantity(productId) == 0 && movementType.equals(MovementType.OUT)) {
            throw new UnavailableMovement("The product is not available");
        }
    }
}
