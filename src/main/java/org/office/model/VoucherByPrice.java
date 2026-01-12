package org.office.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "VoucherByPrice")
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "voucherByPriceId")
public class VoucherByPrice extends Voucher {
    
    @Column(name = "lowerbound")
    private Double minOrderValue;
    
    @Column(name = "discountPercent")
    private Double discountPercent;
    
    @Column(name = "maxDiscount")
    private Double maxDiscount;
}

