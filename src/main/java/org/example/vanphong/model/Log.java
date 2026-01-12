package org.example.vanphong.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "log")
@Data
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logId")
    private Integer logId;

    private String content;
    
    private Date dateLog;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}

