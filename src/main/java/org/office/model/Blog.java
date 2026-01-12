package org.office.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "blog")
@Data
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
    private Integer blogId;

    @Column(name = "blog_title")
    private String blogTitle;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "posting_date")
    @Temporal(TemporalType.DATE)
    private Date postingDate;
    
    private Integer approval;
}

