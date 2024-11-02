package com.stockmanager.infrastructure.adapter.in.web;

import com.stockmanager.application.dto.MoveStockRequest;
import com.stockmanager.application.dto.ProductDTO;
import com.stockmanager.application.port.in.ProductUseCase;
import com.stockmanager.application.port.in.StockUseCase;
import com.stockmanager.domain.enums.MovementType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final ProductUseCase productUseCase;
    private final StockUseCase stockUseCase;

    @PostMapping
    @Transactional
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO body) {
        stockUseCase.addProduct(body);
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts() {
        return ResponseEntity.ok(productUseCase.getProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productUseCase.getProduct(id));
    }


    @PutMapping
    @Transactional
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO body) {
        productUseCase.updateProduct(body);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PutMapping("/move-stock")
    @Transactional
    public ResponseEntity<?> moveStock(@RequestBody MoveStockRequest request) {
        stockUseCase.moveStock(request.productId(), request.quantity(), MovementType.valueOf(request.movementType()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
