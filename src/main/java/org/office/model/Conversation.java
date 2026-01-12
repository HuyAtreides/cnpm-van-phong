package org.office.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "conversations")
@Data
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversationId")
    private Integer conversationId;

    @ManyToOne
    @JoinColumn(name = "customerId")
    @lombok.ToString.Exclude
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "employeeId")
    @lombok.ToString.Exclude
    private Employee employee;

    @Column(columnDefinition = "NVARCHAR(255)") 
    private String title;
    
    private String status;
    
    private java.util.Date createdDate;
    
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    @lombok.ToString.Exclude
    private List<Message> messages;
}

