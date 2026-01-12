package org.office.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;

@Entity
@Table(name = "refund_requests")
@Data
@NoArgsConstructor
public class RefundRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer refundId;

    @ManyToOne
    @JoinColumn(name = "orderId")
    @ToString.Exclude
    private Order order;

    
    
    @ManyToOne
    @JoinColumn(name = "productId")
    @ToString.Exclude
    private Product product;

    private String reason;
    private String requestType; 
    private String status; 
    private String imagePath;
    
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        if (status == null) status = "PENDING";
    }
}

