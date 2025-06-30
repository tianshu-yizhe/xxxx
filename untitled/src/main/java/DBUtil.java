package com.property;

import com.property.managers.AuthManager;
import com.property.models.User;
import java.sql.*;
import javax.swing.*;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/property_management?useSSL=false&serverTimezone=Asia/Shanghai";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[成功] MySQL JDBC驱动已加载");
        } catch (ClassNotFoundException e) {
            showErrorDialog("驱动加载失败", "缺少MySQL连接驱动jar包\n请添加mysql-connector-java-8.0.x.jar");
            e.printStackTrace();
        }
    }

    public static boolean testConnection() {
        System.out.println("正在测试数据库连接...");
        try (Connection conn = getConnection()) {
            initializeAdmin(); // 检查并创建默认管理员
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            printSQLException(e);
            return false;
        }
    }

    public static void initializeAdmin() {
        try (Connection conn = getConnection()) {
            AuthManager authManager = new AuthManager();
            if (!authManager.hasAnyAdmin()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("admin123");
                admin.setRole(User.Role.ADMIN);
                if (authManager.register(admin)) {
                    System.out.println("默认管理员账号已创建: admin/admin123");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("数据库连接成功");
            return conn;
        } catch (SQLException e) {
            System.err.println("数据库连接失败");
            throw e;
        }
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("数据库连接已关闭");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printSQLException(SQLException ex) {
        System.err.println("----- SQL异常信息 -----");
        System.err.println("错误代码: " + ex.getErrorCode());
        System.err.println("SQL状态: " + ex.getSQLState());
        System.err.println("错误信息: " + ex.getMessage());

        if (ex.getMessage().contains("Access denied")) {
            System.err.println("→ 用户名或密码错误");
        } else if (ex.getMessage().contains("Unknown database")) {
            System.err.println("→ 数据库不存在，请创建: " + URL.split("/")[3].split("\\?")[0]);
        }
    }

    private static void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.ERROR_MESSAGE);
    }

    public static void closeAll(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
