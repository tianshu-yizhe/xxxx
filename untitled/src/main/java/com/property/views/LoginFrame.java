package com.property.views;

import com.property.managers.AuthManager;
import com.property.models.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("物业管理系统 - 登录");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("物业管理系统", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        panel.add(titleLabel, gbc);

        // 用户名
        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(new JLabel("用户名:"), gbc);
        gbc.gridx++;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        // 密码
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("密码:"), gbc);
        gbc.gridx++;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // 按钮
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        JButton loginBtn = new JButton("登录");
        loginBtn.setPreferredSize(new Dimension(100, 30));
        panel.add(loginBtn, gbc);

        add(panel);

        // 事件监听
        loginBtn.addActionListener(this::login);
    }

    private void login(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            AuthManager authManager = new AuthManager();
            User user = authManager.login(username, password);
            if (user != null) {
                if (user.getRole() == User.Role.RESIDENT && user.getResidentId() == null) {
                    JOptionPane.showMessageDialog(this,
                            "住户账户未关联住户信息，请联系管理员",
                            "错误",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                dispose();
                if (user.isAdmin()) {
                    new AdminFrame(user).setVisible(true);
                } else {
                    new UserFrame(user).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "用户名或密码错误", "登录失败", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "数据库错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}