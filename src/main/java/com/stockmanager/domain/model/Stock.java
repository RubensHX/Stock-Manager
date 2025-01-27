package com.stockmanager.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", product=" + product +
                ", availableQuantity=" + availableQuantity +
                ", minimumQuantity=" + minimumQuantity +
                ", location='" + location + '\'' +
                ", stockMovements=" + stockMovements +
                '}';
    }
}
