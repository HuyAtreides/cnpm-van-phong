package org.office.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "payment_account")
@Data
public class PaymentAccount {
    @Id
    @Column(name = "bankId")
    private Integer bankId;

    private String bank;
    private String serialNumber;
    private String type;
}

