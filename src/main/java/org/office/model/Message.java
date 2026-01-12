package org.office.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "messages")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "messageId")
    private Integer messageId; 

    @Column(columnDefinition = "TEXT")
    private String content;
    
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "conversationId")
    @lombok.ToString.Exclude
    private Conversation conversation;
    
    @ManyToOne
    @JoinColumn(name = "userId")
    @lombok.ToString.Exclude
    private User sender;
}

