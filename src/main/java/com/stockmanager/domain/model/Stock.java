package com.stockmanager.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Product product;

    private Integer availableQuantity;

    private Integer minimumQuantity;

    private String location;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stock")
    private Set<StockMovement> stockMovements = new HashSet<>(0);
}
