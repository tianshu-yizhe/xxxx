package com.property.managers;

import com.property.DBUtil;
import com.property.models.Facility;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacilityManager {
    public List<Facility> getAllFacilities() throws SQLException {
        List<Facility> facilities = new ArrayList<>();
        String sql = "SELECT * FROM facilities ORDER BY name";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Facility facility = new Facility();
                facility.setId(rs.getInt("id"));
                facility.setName(rs.getString("name"));
                facility.setDescription(rs.getString("description"));
                facility.setStatus(rs.getString("status"));
                facility.setLocation(rs.getString("location"));
                facility.setCreatedAt(rs.getTimestamp("created_at"));
                facilities.add(facility);
            }
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }
        return facilities;
    }

    public boolean addFacility(Facility facility) throws SQLException {
        String sql = "INSERT INTO facilities (name, description, status, location) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, facility.getName());
            stmt.setString(2, facility.getDescription());
            stmt.setString(3, facility.getStatus());
            stmt.setString(4, facility.getLocation());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    facility.setId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }
    }

    public boolean updateFacilityStatus(int facilityId, String status) throws SQLException {
        String sql = "UPDATE facilities SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, facilityId);
            return stmt.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }
    }

    public boolean deleteFacility(int facilityId) throws SQLException {
        String sql = "DELETE FROM facilities WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, facilityId);
            return stmt.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }
    }

    // 添加设施状态检查方法
    public boolean isFacilityAvailable(int facilityId) throws SQLException {
        String sql = "SELECT status FROM facilities WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, facilityId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("status");
                    return "AVAILABLE".equals(status);
                }
                return false;
            }
        }
    }
}