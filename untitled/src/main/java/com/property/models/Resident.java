package com.property.models;

import java.sql.Timestamp;

public class Resident {
    private int id;
    private String name;
    private String phone;
    private String roomNumber;
    private String identityNumber;
    private Timestamp createdAt;

    // 无参构造器
    public Resident() {}

    // 全参构造器
    public Resident(int id, String name, String phone, String roomNumber, String identityNumber, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.roomNumber = roomNumber;
        this.identityNumber = identityNumber;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Resident{" +
                "id=" + id +
               ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                '}';
    }
}