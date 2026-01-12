package org.office.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_type")
@Data
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "typeId")
    private Integer typeId;

    private String color;
    private String material;
    private Double price;
    private Integer quantity;
    
    private Double height;
    private Double width;
    private Double length;
    private Double weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private Product product;
}

