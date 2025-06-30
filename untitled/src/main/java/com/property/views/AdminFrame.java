package com.property.views;

import com.property.DBUtil;
import com.property.managers.AuthManager;
import com.property.managers.ResidentManager;
import com.property.models.Resident;
import com.property.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminFrame extends JFrame {
    public AdminFrame(User user) {
        setTitle("物业管理系统 - 管理员面板");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建选项卡面板
        JTabbedPane tabbedPane = new JTabbedPane();
        try {
            tabbedPane.addTab("用户管理", new UserManagementPanel());
            tabbedPane.addTab("住户管理", new ResidentPanel());
            tabbedPane.addTab("设施管理", new FacilityPanel());
            tabbedPane.addTab("费用管理", new FeePanel());
            tabbedPane.addTab("报修管理", new RepairPanel());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "初始化失败: " + e.getMessage(),
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
        }

        // 用户信息面板
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.add(new JLabel("欢迎, " + user.getUsername() + " (管理员)"));
        JButton logoutBtn = new JButton("退出登录");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        userPanel.add(logoutBtn);

        // 添加组件到窗口
        add(userPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    class UserManagementPanel extends JPanel {
        private JTable userTable;
        private DefaultTableModel tableModel;
        private AuthManager authManager;
        private ResidentManager residentManager;

        public UserManagementPanel() throws SQLException {
            authManager = new AuthManager();
            residentManager = new ResidentManager();
            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // 工具栏
            JToolBar toolBar = new JToolBar();
            toolBar.setFloatable(false);
            JButton addAdminBtn = new JButton("添加管理员");
            JButton addResidentBtn = new JButton("添加住户");
            JButton deleteBtn = new JButton("删除用户");
            JButton refreshBtn = new JButton("刷新");

            Dimension btnSize = new Dimension(120, 30);
            addAdminBtn.setPreferredSize(btnSize);
            addResidentBtn.setPreferredSize(btnSize);
            deleteBtn.setPreferredSize(btnSize);
            refreshBtn.setPreferredSize(btnSize);

            toolBar.add(addAdminBtn);
            toolBar.add(addResidentBtn);
            toolBar.add(deleteBtn);
            toolBar.addSeparator();
            toolBar.add(refreshBtn);
            add(toolBar, BorderLayout.SOUTH);

            // 表格
            tableModel = new DefaultTableModel(
                    new Object[]{"ID", "用户名", "角色", "关联住户ID", "创建时间"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            userTable = new JTable(tableModel);
            userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(userTable);
            add(scrollPane, BorderLayout.CENTER);

            // 加载数据
            loadUsers();

            // 添加事件监听
            addAdminBtn.addActionListener(e -> addAdmin());
            addResidentBtn.addActionListener(e -> addResident());
            deleteBtn.addActionListener(e -> deleteUser());
            refreshBtn.addActionListener(e -> {
                try {
                    loadUsers();
                } catch (SQLException ex) {
                    showError("刷新失败: " + ex.getMessage());
                }
            });
        }

        private void loadUsers() throws SQLException {
            tableModel.setRowCount(0);
            String sql = "SELECT * FROM users ORDER BY role, username";
            try (Connection conn = DBUtil.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("role"),
                            rs.getObject("resident_id"),
                            rs.getTimestamp("created_at")
                    });
                }
            }
        }

        private void addAdmin() {
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            JPasswordField confirmField = new JPasswordField();

            Object[] message = {
                    "用户名:", usernameField,
                    "密码:", passwordField,
                    "确认密码:", confirmField
            };

            int option = JOptionPane.showConfirmDialog(
                    this,
                    message,
                    "添加管理员",
                    JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String confirm = new String(confirmField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    showError("用户名和密码不能为空");
                    return;
                }

                if (!password.equals(confirm)) {
                    showError("两次输入的密码不一致");
                    return;
                }

                try {
                    if (authManager.usernameExists(username)) {
                        showError("用户名已存在");
                        return;
                    }

                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setRole(User.Role.ADMIN);

                    if (authManager.register(user)) {
                        loadUsers();
                        JOptionPane.showMessageDialog(this, "管理员添加成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        showError("添加管理员失败");
                    }
                } catch (SQLException ex) {
                    showError("数据库错误: " + ex.getMessage());
                }
            }
        }

        private void addResident() {
            // 先添加住户信息
            JTextField nameField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField roomField = new JTextField();
            JTextField idField = new JTextField();
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            JPasswordField confirmField = new JPasswordField();

            Object[] message = {
                    "住户姓名:", nameField,
                    "电话:", phoneField,
                    "房间号:", roomField,
                    "身份证号:", idField,
                    "用户名:", usernameField,
                    "密码:", passwordField,
                    "确认密码:", confirmField
            };

            int option = JOptionPane.showConfirmDialog(
                    this,
                    message,
                    "添加住户用户",
                    JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                try {
                    // 1. 先添加住户信息
                    Resident resident = new Resident();
                    resident.setName(nameField.getText().trim());
                    resident.setPhone(phoneField.getText().trim());
                    resident.setRoomNumber(roomField.getText().trim());
                    resident.setIdentityNumber(idField.getText().trim());

                    if (resident.getName().isEmpty() || resident.getPhone().isEmpty() ||
                            resident.getRoomNumber().isEmpty() || resident.getIdentityNumber().isEmpty()) {
                        showError("所有住户信息字段都必须填写");
                        return;
                    }

                    if (!residentManager.addResident(resident)) {
                        showError("添加住户信息失败");
                        return;
                    }

                    // 2. 添加用户信息
                    String username = usernameField.getText().trim();
                    String password = new String(passwordField.getPassword());
                    String confirm = new String(confirmField.getPassword());

                    if (username.isEmpty() || password.isEmpty()) {
                        showError("用户名和密码不能为空");
                        return;
                    }

                    if (!password.equals(confirm)) {
                        showError("两次输入的密码不一致");
                        return;
                    }

                    if (authManager.usernameExists(username)) {
                        showError("用户名已存在");
                        return;
                    }

                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setRole(User.Role.RESIDENT);
                    user.setResidentId(resident.getId()); // 确保关联住户ID

                    if (authManager.register(user)) {
                        loadUsers();
                        JOptionPane.showMessageDialog(this, "住户用户添加成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        showError("添加住户用户失败");
                    }
                } catch (SQLException ex) {
                    showError("数据库错误: " + ex.getMessage());
                }
            }
        }

        private void deleteUser() {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                showError("请先选择要删除的用户");
                return;
            }

            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            String username = (String) tableModel.getValueAt(selectedRow, 1);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "确定要删除用户 '" + username + "' 吗?",
                    "确认删除",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    String sql = "DELETE FROM users WHERE id = ?";
                    try (Connection conn = DBUtil.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setInt(1, userId);
                        if (stmt.executeUpdate() > 0) {
                            loadUsers();
                            JOptionPane.showMessageDialog(this, "用户删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            showError("删除用户失败");
                        }
                    }
                } catch (SQLException ex) {
                    showError("数据库错误: " + ex.getMessage());
                }
            }
        }

        private void showError(String message) {
            JOptionPane.showMessageDialog(this, message, "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}