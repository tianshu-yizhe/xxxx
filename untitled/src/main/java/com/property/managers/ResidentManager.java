package com.property.managers;

import com.property.DBUtil;
import com.property.models.Resident;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResidentManager {
    /**
     * 获取所有住户信息
     */
    public List<Resident> getAllResidents() throws SQLException {
        List<Resident> residents = new ArrayList<>();
        String sql = "SELECT * FROM residents ORDER BY room_number";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Resident resident = new Resident();
                resident.setId(rs.getInt("id"));
                resident.setName(rs.getString("name"));
                resident.setPhone(rs.getString("phone"));
                resident.setRoomNumber(rs.getString("room_number"));
                resident.setIdentityNumber(rs.getString("identity_number"));
                resident.setCreatedAt(rs.getTimestamp("created_at"));
                residents.add(resident);
            }
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }
        return residents;
    }

    /**
     * 根据ID获取住户
     */
    public Resident getResidentById(int id) throws SQLException {
        String sql = "SELECT * FROM residents WHERE id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Resident resident = new Resident();
                resident.setId(rs.getInt("id"));
                resident.setName(rs.getString("name"));
                resident.setPhone(rs.getString("phone"));
                resident.setRoomNumber(rs.getString("room_number"));
                resident.setIdentityNumber(rs.getString("identity_number"));
                return resident;
            }
            return null;
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }
    }

    /**
     * 搜索住户
     */
    public List<Resident> searchResidents(String keyword) throws SQLException {
        List<Resident> residents = new ArrayList<>();
        String sql = "SELECT * FROM residents WHERE name LIKE ? OR room_number LIKE ? OR phone LIKE ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            String likeKeyword = "%" + keyword + "%";
            stmt.setString(1, likeKeyword);
            stmt.setString(2, likeKeyword);
            stmt.setString(3, likeKeyword);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Resident resident = new Resident();
                resident.setId(rs.getInt("id"));
                resident.setName(rs.getString("name"));
                resident.setPhone(rs.getString("phone"));
                resident.setRoomNumber(rs.getString("room_number"));
                residents.add(resident);
            }
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }
        return residents;
    }

    /**
     * 添加新住户
     */
    public boolean addResident(Resident resident) throws SQLException {
        String sql = "INSERT INTO residents (name, phone, room_number, identity_number) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, resident.getName());
            stmt.setString(2, resident.getPhone());
            stmt.setString(3, resident.getRoomNumber());
            stmt.setString(4, resident.getIdentityNumber());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    resident.setId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }
    }

    /**
     * 更新住户信息
     */
    public boolean updateResident(Resident resident) throws SQLException {
        String sql = "UPDATE residents SET name = ?, phone = ?, room_number = ?, identity_number = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, resident.getName());
            stmt.setString(2, resident.getPhone());
            stmt.setString(3, resident.getRoomNumber());
            stmt.setString(4, resident.getIdentityNumber());
            stmt.setInt(5, resident.getId());

            return stmt.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }
    }

    /**
     * 删除住户
     */
    public boolean deleteResident(int residentId) throws SQLException {
        String sql = "DELETE FROM residents WHERE id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, residentId);
            return stmt.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }
    }
}