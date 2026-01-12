package org.example.vanphong.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "wishlists")
@Data
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlistId")
    private Integer wishlistId;

    private Integer isDelete;

    @OneToOne
    @JoinColumn(name = "customerId")
    @lombok.ToString.Exclude
    private Customer customer;

    @ManyToMany
    @JoinTable(
        name = "wishlists_products",
        joinColumns = @JoinColumn(name = "wishlistId"),
        inverseJoinColumns = @JoinColumn(name = "productId")
    )
    private List<Product> products = new ArrayList<>();
}

