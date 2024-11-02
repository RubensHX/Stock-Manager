package com.stockmanager.domain.model;

import com.stockmanager.domain.enums.MovementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "stock_movement")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Stock stock;

    @Temporal(TemporalType.TIMESTAMP)
    private Date movementDate;

    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    private Integer quantity;

    private String observation;

    public StockMovement(Stock stock, Integer quantity, MovementType movementType) {
        this.stock = stock;
        this.movementDate = new Date();
        this.quantity = quantity;
        this.movementType = movementType;
    }
}
