package com.property;

import com.property.views.LoginFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // 测试数据库连接
        if (!DBUtil.testConnection()) {
            JOptionPane.showMessageDialog(
                    null,
                    "数据库连接失败，请检查:\n1. MySQL服务是否启动\n2. 数据库配置是否正确",
                    "数据库错误",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // 启动界面
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new LoginFrame().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        null,
                        "系统初始化失败: " + e.getMessage(),
                        "错误",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}


