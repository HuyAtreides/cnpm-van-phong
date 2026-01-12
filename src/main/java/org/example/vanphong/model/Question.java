package org.example.vanphong.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "question")
@Data
public class Question {
    @Id
    @Column(name = "questionId")
    private Integer questionId;

    private String content;
    private Integer isPending;
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}

