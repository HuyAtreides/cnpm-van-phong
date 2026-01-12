package org.office.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "staff")
@PrimaryKeyJoinColumn(name = "userId")
@Data
@EqualsAndHashCode(callSuper = true)
public class Staff extends User {
    private String position;
    private String department;
}

