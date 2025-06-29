package com.property.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "fees")
@Data
public class Fee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "resident_id", nullable = false)
    private Integer residentId;

    @Column(nullable = false)
    private String type; // PROPERTY, WATER, ELECTRICITY, PARKING, OTHER

    @Column(nullable = false)
    private Double amount;

    @Column(name = "due_date", nullable = false)
    private Date dueDate;

    @Column(nullable = false)
    private String status; // UNPAID, PAID, OVERDUE

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;
}