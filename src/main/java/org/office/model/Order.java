package org.office.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    private Integer orderId;

    @Column(name = "orderDate")
    @Temporal(TemporalType.DATE)
    private Date orderDate;

    private String status; 
    private Double totalCost;
    private Double discount;
    private Double actualCost; 

    
    private String cityOfProvince;
    private String district;
    private String ward;
    private String streetNumber;
    private String phone;
    
    @Column(name = "paymentMethod")
    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private List<OrderItem> orderItems;
}

