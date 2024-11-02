package com.stockmanager.application.dto;

import java.util.Set;

public record ProductDTO(Long id, String name, String description, Double price,
                         Set<ProductCategoryDTO> categories) {
}
