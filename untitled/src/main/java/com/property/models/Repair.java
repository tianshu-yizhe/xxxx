package com.property.models;

import java.sql.Timestamp;

public class Repair {
    private int id;
    private int residentId;
    private String title;
    private String description;
    private String status; // PENDING, PROCESSING, COMPLETED
    private String staffNotes;
    private Timestamp createdAt;
    private Timestamp completedAt;

    // 无参构造器
    public Repair() {}

    // 全参构造器
    public Repair(int id, int residentId, String title, String description, String status,
                  String staffNotes, Timestamp createdAt, Timestamp completedAt) {
        this.id = id;
        this.residentId = residentId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.staffNotes = staffNotes;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }

    // Getter和Setter方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResidentId() {
        return residentId;
    }

    public void setResidentId(int residentId) {
        this.residentId = residentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStaffNotes() {
        return staffNotes;
    }

    public void setStaffNotes(String staffNotes) {
        this.staffNotes = staffNotes;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Timestamp completedAt) {
        this.completedAt = completedAt;
    }

    @Override
    public String toString() {
        return "Repair{" +
                "id=" + id +
                ", residentId=" + residentId +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}