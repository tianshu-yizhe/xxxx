package com.property.managers;

import com.property.DBUtil;
import com.property.models.Fee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeeManager {

    /**
     * 根据住户ID获取费用列表
     * @param residentId 住户ID
     * @return 费用列表
     * @throws SQLException 数据库异常
     */
    public List<Fee> getFeesByResidentId(int residentId) throws SQLException {
        List<Fee> fees = new ArrayList<>();
        String sql = "SELECT * FROM fees WHERE resident_id = ? ORDER BY due_date DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, residentId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Fee fee = new Fee();
                    fee.setId(rs.getInt("id"));
                    fee.setResidentId(rs.getInt("resident_id"));
                    fee.setType(rs.getString("type"));
                    fee.setAmount(rs.getDouble("amount"));
                    fee.setDueDate(rs.getDate("due_date"));
                    fee.setStatus(rs.getString("status"));
                    fee.setCreatedAt(rs.getTimestamp("created_at"));
                    fees.add(fee);
                }
            }
        }
        return fees;
    }

    /**
     * 获取所有未支付的费用
     * @return 未支付费用列表
     * @throws SQLException 数据库异常
     */
    public List<Fee> getUnpaidFees() throws SQLException {
        List<Fee> fees = new ArrayList<>();
        String sql = "SELECT * FROM fees WHERE status = 'UNPAID' ORDER BY due_date";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Fee fee = new Fee();
                fee.setId(rs.getInt("id"));
                fee.setResidentId(rs.getInt("resident_id"));
                fee.setType(rs.getString("type"));
                fee.setAmount(rs.getDouble("amount"));
                fee.setDueDate(rs.getDate("due_date"));
                fee.setStatus(rs.getString("status"));
                fee.setCreatedAt(rs.getTimestamp("created_at"));
                fees.add(fee);
            }
        }
        return fees;
    }

    /**
     * 添加新费用
     * @param fee 费用对象
     * @return 是否添加成功
     * @throws SQLException 数据库异常
     */
    public boolean addFee(Fee fee) throws SQLException {
        String sql = "INSERT INTO fees (resident_id, type, amount, due_date, status) VALUES (?, ?, ?, ?, ?)";
        boolean success = false;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, fee.getResidentId());
            stmt.setString(2, fee.getType());
            stmt.setDouble(3, fee.getAmount());
            stmt.setDate(4, new java.sql.Date(fee.getDueDate().getTime()));
            stmt.setString(5, fee.getStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        fee.setId(rs.getInt(1));
                    }
                }
                success = true;
            }
        }
        return success;
    }

    /**
     * 更新费用状态
     * @param feeId 费用ID
     * @param status 新状态
     * @return 是否更新成功
     * @throws SQLException 数据库异常
     */
    public boolean updateFeeStatus(int feeId, String status) throws SQLException {
        String sql = "UPDATE fees SET status = ? WHERE id = ?";
        boolean success = false;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, feeId);

            success = stmt.executeUpdate() > 0;
        }
        return success;
    }
}