package com.stockmanager.domain.model;

import com.stockmanager.domain.enums.MovementType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@EqualsAndHashCode
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

    @Override
    public String toString() {
        return "StockMovement{" +
                "id=" + id +
                ", stock=" + stock +
                ", movementDate=" + movementDate +
                ", movementType=" + movementType +
                ", quantity=" + quantity +
                ", observation='" + observation + '\'' +
                '}';
    }
}
