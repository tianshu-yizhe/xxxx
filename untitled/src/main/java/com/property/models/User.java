package com.property.models;

import java.sql.Timestamp;

public class User {
    public enum Role {
        ADMIN, RESIDENT
    }

    private int id;
    private String username;
    private String password;
    private Role role;
    private Integer residentId;
    private Timestamp createdAt;

    // 无参构造器
    public User() {}

    // 全参构造器
    public User(int id, String username, String password, Role role, Integer residentId, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.residentId = residentId;
        this.createdAt = createdAt;
    }

    // Getter和Setter方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getResidentId() {
        return residentId;
    }

    public void setResidentId(Integer residentId) {
        this.residentId = residentId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // 实用方法
    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", residentId=" + residentId +
                '}';
    }
}