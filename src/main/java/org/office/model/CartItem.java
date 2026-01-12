package org.office.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CartItem")
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartItemId")
    private Integer cartItemId;

    @ManyToOne
    @JoinColumn(name = "cartId")
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "typeId")
    private ProductType productType;

    private Integer quantity;
    private Double price;
}

