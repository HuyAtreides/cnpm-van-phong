package org.office.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_image")
@Data
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productImageId")
    private Integer productImageId;

    @Column(name = "productImage")
    private String productImage; 

    @ManyToOne
    @JoinColumn(name = "productId")
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private Product product;
}

