package com.property.models;

import java.sql.Timestamp;

public class Facility {
    private int id;
    private String name;
    private String description;
    private String status; // AVAILABLE, MAINTENANCE, OUT_OF_SERVICE
    private String location;
    private Timestamp createdAt;

    // 无参构造器
    public Facility() {}

    // 全参构造器
    public Facility(int id, String name, String description, String status, String location, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.location = location;
        this.createdAt = createdAt;
    }

    // Getter和Setter方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Facility{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}