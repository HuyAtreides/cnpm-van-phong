package org.example.vanphong.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressId;

    @ManyToOne
    @JoinColumn(name = "userId")
    @ToString.Exclude
    private User user;

    private String receiverName;
    private String phone;
    private String city;
    private String district;
    private String ward;
    private String specificAddress;
    
    @Column(columnDefinition = "boolean default false")
    private boolean isDefault;

    public String getFullAddress() {
        return specificAddress + ", " + ward + ", " + district + ", " + city;
    }
}

