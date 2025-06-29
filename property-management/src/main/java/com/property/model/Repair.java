package com.property.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "repairs")
@Data
public class Repair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "resident_id", nullable = false)
    private Integer residentId;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private String status; // PENDING, PROCESSING, COMPLETED

    @Column(name = "staff_notes")
    private String staffNotes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "completed_at")
    private Timestamp completedAt;
}