package com.property.managers;

import com.property.DBUtil;
import com.property.models.Repair;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepairManager {
    public List<Repair> getRepairsByResidentId(int residentId) throws SQLException {
        List<Repair> repairs = new ArrayList<>();
        String sql = "SELECT * FROM repairs WHERE resident_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, residentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Repair repair = new Repair();
                    repair.setId(rs.getInt("id"));
                    repair.setResidentId(rs.getInt("resident_id"));
                    repair.setTitle(rs.getString("title"));
                    repair.setDescription(rs.getString("description"));
                    repair.setStatus(rs.getString("status"));
                    repair.setStaffNotes(rs.getString("staff_notes"));
                    repair.setCreatedAt(rs.getTimestamp("created_at"));
                    repair.setCompletedAt(rs.getTimestamp("completed_at"));
                    repairs.add(repair);
                }
            }
        }
        return repairs;
    }

    public List<Repair> getRepairsByStatus(String status) throws SQLException {
        List<Repair> repairs = new ArrayList<>();
        String sql = "SELECT * FROM repairs WHERE status = ? ORDER BY created_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Repair repair = new Repair();
                    repair.setId(rs.getInt("id"));
                    repair.setResidentId(rs.getInt("resident_id"));
                    repair.setTitle(rs.getString("title"));
                    repair.setDescription(rs.getString("description"));
                    repair.setStatus(rs.getString("status"));
                    repair.setStaffNotes(rs.getString("staff_notes"));
                    repair.setCreatedAt(rs.getTimestamp("created_at"));
                    repair.setCompletedAt(rs.getTimestamp("completed_at"));
                    repairs.add(repair);
                }
            }
        }
        return repairs;
    }

    public boolean addRepair(Repair repair) throws SQLException {
        String sql = "INSERT INTO repairs (resident_id, title, description, status) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, repair.getResidentId());
            stmt.setString(2, repair.getTitle());
            stmt.setString(3, repair.getDescription());
            stmt.setString(4, repair.getStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    repair.setId(rs.getInt(1));
                }
                return true;
            }
            return false;
        } finally {
            DBUtil.closeAll(conn, stmt, rs);
        }
    }

    public boolean updateRepairStatus(int repairId, String status, String staffNotes) throws SQLException {
        String sql = "UPDATE repairs SET status = ?, staff_notes = ?, completed_at = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setString(2, staffNotes);
            stmt.setTimestamp(3, "COMPLETED".equals(status) ? new Timestamp(System.currentTimeMillis()) : null);
            stmt.setInt(4, repairId);

            return stmt.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(conn, stmt, null);
        }
    }
}