package com.stockmanager.infrastructure.config;

import com.stockmanager.application.port.out.ProductRepository;
import com.stockmanager.application.port.out.StockRepository;
import com.stockmanager.application.service.StockService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public StockService stockService(ProductRepository productRepository, StockRepository stockRepository) {
        return new StockService(productRepository, stockRepository);
    }

}