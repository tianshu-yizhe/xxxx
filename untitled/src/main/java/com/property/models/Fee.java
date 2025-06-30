package com.property.models;

import java.sql.Date;
import java.sql.Timestamp;

public class Fee {
    private int id;
    private int residentId;
    private String type; // PROPERTY, WATER, ELECTRICITY, PARKING, OTHER
    private double amount;
    private Date dueDate;
    private String status; // UNPAID, PAID, OVERDUE
    private Timestamp createdAt;

    // 无参构造器
    public Fee() {}

    // 全参构造器
    public Fee(int id, int residentId, String type, double amount, Date dueDate, String status, Timestamp createdAt) {
        this.id = id;
        this.residentId = residentId;
        this.type = type;
        this.amount = amount;
        this.dueDate = dueDate;
        this.status = status;
        this.createdAt = createdAt;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Fee{" +
                "id=" + id +
                ", residentId=" + residentId +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", dueDate=" + dueDate +
                ", status='" + status + '\'' +
                '}';
    }
}