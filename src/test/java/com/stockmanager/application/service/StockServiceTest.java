package com.stockmanager.application.service;

import com.stockmanager.application.dto.ProductCategoryDTO;
import com.stockmanager.application.dto.ProductDTO;
import com.stockmanager.application.mapper.ProductMapper;
import com.stockmanager.application.port.out.ProductRepository;
import com.stockmanager.application.port.out.StockRepository;
import com.stockmanager.domain.enums.MovementType;
import com.stockmanager.domain.model.Product;
import com.stockmanager.domain.model.Stock;
import com.stockmanager.infrastructure.advice.NotFoundException;
import com.stockmanager.infrastructure.advice.UnavailableMovement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockServiceTest {

    @InjectMocks
    StockService stockService;

    @Mock
    ProductRepository productRepository;

    @Mock
    StockRepository stockRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stockService = new StockService(productRepository, stockRepository);
    }

    @Test
    void getProductReturnsProductDTOWhenProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(new Product()));
        ProductDTO productDTO = stockService.getProduct(1L);
        assertNotNull(productDTO);
    }

    @Test
    void getProductThrowsNotFoundExceptionWhenProductDoesNotExist() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> stockService.getProduct(1L));
    }

    @Test
    void updateProductSavesProductWhenValidDTO() {
        ProductDTO productDTO = new ProductDTO(
                1L,
                "Product",
                "Description",
                10.0,
                new HashSet<>(Arrays.asList(new ProductCategoryDTO(1L, "tag1", "desc"), new ProductCategoryDTO(1L, "tag2", "desc")))
        );
        Product product = new Product();
        ProductMapper productMapperMock = mock(ProductMapper.class);
        when(productMapperMock.toModel(productDTO)).thenReturn(product);

        stockService.updateProduct(productDTO);

        verify(productRepository, times(1)).save(product);
    }

    @Test
    void updateProductThrowsExceptionWhenDTOIsNull() {
        assertThrows(IllegalArgumentException.class, () -> stockService.updateProduct(null));
    }

    @Test
    void getProductsReturnsListOfProductDTOsWhenProductsExist() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(productRepository.findAll()).thenReturn(products);

        List<ProductDTO> productDTOs = stockService.getProducts();

        assertNotNull(productDTOs);
        assertEquals(2, productDTOs.size());
    }

    @Test
    void getProductsReturnsEmptyListWhenNoProductsExist() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        List<ProductDTO> productDTOs = stockService.getProducts();

        assertNotNull(productDTOs);
        assertTrue(productDTOs.isEmpty());
    }

    @Test
    void addProductSavesProductWithInitialStock() {
        ProductDTO productDTO = new ProductDTO(
                1L,
                "Product",
                "Description",
                10.0,
                new HashSet<>(Arrays.asList(new ProductCategoryDTO(1L, "tag1", "desc"), new ProductCategoryDTO(1L, "tag2", "desc")))
        );
        Product product = new Product();
        product.setName("Product");
        product.setDescription("Description");
        product.setPrice(10.0);
        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setAvailableQuantity(0);
        stock.setMinimumQuantity(0);
        product.setStock(stock);
        product.setCategories(new HashSet<>());

        ProductMapper productMapperMock = mock(ProductMapper.class);
        when(productMapperMock.toModel(productDTO)).thenReturn(product);

        stockService.addProduct(productDTO);

        verify(productRepository, times(1)).save(product);
        assertEquals(0, product.getStock().getAvailableQuantity());
        assertEquals(0, product.getStock().getMinimumQuantity());
    }

    @Test
    void addProductThrowsExceptionWhenDTOIsNull() {
        assertThrows(NullPointerException.class, () -> stockService.addProduct(null));
    }

    @Test
    void moveStockIncreasesAvailableQuantityWhenMovementTypeIsIn() {
        Stock stock = new Stock();
        stock.setAvailableQuantity(10);
        when(stockRepository.findByProductId(1L)).thenReturn(Optional.of(stock));

        stockService.moveStock(1L, 5, MovementType.IN);

        assertEquals(15, stock.getAvailableQuantity());
        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    void moveStockDecreasesAvailableQuantityWhenMovementTypeIsOut() {
        Stock stock = new Stock();
        stock.setAvailableQuantity(10);
        when(stockRepository.findByProductId(1L)).thenReturn(Optional.of(stock));

        stockService.moveStock(1L, 5, MovementType.OUT);

        assertEquals(5, stock.getAvailableQuantity());
        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    void moveStockThrowsNotFoundExceptionWhenStockDoesNotExist() {
        when(stockRepository.findByProductId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> stockService.moveStock(1L, 5, MovementType.IN));
    }

    @Test
    void moveStockThrowsUnavailableMovementWhenStockIsNotAvailable() {
        Stock stock = new Stock();
        stock.setAvailableQuantity(0);
        when(stockRepository.findByProductId(1L)).thenReturn(Optional.of(stock));

        assertThrows(UnavailableMovement.class, () -> stockService.moveStock(1L, 5, MovementType.OUT));
    }

    @Test
    void getAvailableQuantityReturnsQuantityWhenStockExists() {
        Stock stock = new Stock();
        stock.setAvailableQuantity(10);
        when(stockRepository.findByProductId(1L)).thenReturn(Optional.of(stock));

        Integer availableQuantity = stockService.getAvailableQuantity(1L);

        assertEquals(10, availableQuantity);
    }

    @Test
    void getAvailableQuantityThrowsNotFoundExceptionWhenStockDoesNotExist() {
        when(stockRepository.findByProductId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> stockService.getAvailableQuantity(1L));
    }

    @Test
    void validateStockMovementThrowsUnavailableMovementWhenStockIsNotAvailable() {
        Stock stock = new Stock();
        stock.setAvailableQuantity(0);
        Product product = new Product();
        product.setId(1L);
        stock.setProduct(product);
        when(stockRepository.findByProductId(1L)).thenReturn(Optional.of(stock));

        assertThrows(UnavailableMovement.class, () -> stockService.validateStockMovement(1L, MovementType.OUT));
    }

    @Test
    void validateStockMovementDoesNotThrowExceptionWhenStockIsAvailable() {
        Stock stock = new Stock();
        stock.setAvailableQuantity(10);
        Product product = new Product();
        product.setId(1L);
        stock.setProduct(product);
        when(stockRepository.findByProductId(1L)).thenReturn(Optional.of(stock));

        assertDoesNotThrow(() -> stockService.validateStockMovement(1L, MovementType.OUT));
    }
}