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
@Entity(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    private Double price;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private Stock stock;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private Set<ProductCategory> categories = new HashSet<>(0);
}
