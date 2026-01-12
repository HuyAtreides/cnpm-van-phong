package org.example.vanphong.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Integer userId;

    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String gender;
    private String status;
    
    @Column(name = "isDelete")
    private Integer isDelete;
    
    @Column(name = "isActive")
    private Integer isActive = 0; 

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleId")
    private Role role;
}

