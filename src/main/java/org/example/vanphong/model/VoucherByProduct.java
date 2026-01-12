package org.example.vanphong.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Entity
@Table(name = "VoucherByProduct")
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "voucherByProductId")
public class VoucherByProduct extends Voucher {
    
    @Column(name = "discountPercent")
    private Double discountPercent;
    
    @ManyToMany
    @JoinTable(
        name = "voucher_product_type",
        joinColumns = @JoinColumn(name = "voucherByProductId"),
        inverseJoinColumns = @JoinColumn(name = "typeId")
    )
    private List<ProductType> productTypes;
}

