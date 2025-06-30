package com.property.views;

import com.property.managers.FeeManager;
import com.property.managers.RepairManager;
import com.property.models.Fee;
import com.property.models.Repair;
import com.property.models.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UserFrame extends JFrame {
    private User currentUser;
    private FeeManager feeManager;
    private RepairManager repairManager;

    public UserFrame(User user) {
        this.currentUser = user;
        this.feeManager = new FeeManager();
        this.repairManager = new RepairManager();

        setTitle("物业管理系统 - 住户面板");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建选项卡面板
        JTabbedPane tabbedPane = new JTabbedPane();
        try {
            tabbedPane.addTab("我的费用", new UserFeePanel());
            tabbedPane.addTab("我的报修", new UserRepairPanel());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "初始化失败: " + e.getMessage(),
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
        }

        // 用户信息面板
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.add(new JLabel("欢迎, " + user.getUsername() + " (住户)"));
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

    class UserFeePanel extends JPanel {
        private DefaultTableModel tableModel;

        public UserFeePanel() throws SQLException {
            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // 工具栏
            JToolBar toolBar = new JToolBar();
            toolBar.setFloatable(false);
            JButton payBtn = new JButton("缴费");
            JButton refreshBtn = new JButton("刷新");

            Dimension btnSize = new Dimension(100, 30);
            payBtn.setPreferredSize(btnSize);
            refreshBtn.setPreferredSize(btnSize);

            toolBar.add(payBtn);
            toolBar.addSeparator();
            toolBar.add(refreshBtn);
            add(toolBar, BorderLayout.SOUTH);

            // 表格
            tableModel = new DefaultTableModel(
                    new Object[]{"ID", "类型", "金额", "缴费状态", "缴费截止日期"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            JTable feeTable = new JTable(tableModel);
            feeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(feeTable);
            add(scrollPane, BorderLayout.CENTER);

            // 加载数据
            loadFees();

            // 添加事件监听
            payBtn.addActionListener(e -> payFee(feeTable));
            refreshBtn.addActionListener(e -> {
                try {
                    loadFees();
                } catch (SQLException ex) {
                    showError("刷新失败: " + ex.getMessage());
                }
            });
        }

        private void loadFees() throws SQLException {
            tableModel.setRowCount(0);
            if (currentUser.getResidentId() == null) {
                showError("当前用户未关联住户信息");
                return;
            }

            List<Fee> fees = feeManager.getFeesByResidentId(currentUser.getResidentId());
            for (Fee fee : fees) {
                tableModel.addRow(new Object[]{
                        fee.getId(),
                        fee.getType(),
                        fee.getAmount(),
                        fee.getStatus(),
                        fee.getDueDate()
                });
            }
        }

        private void payFee(JTable feeTable) {
            int selectedRow = feeTable.getSelectedRow();
            if (selectedRow == -1) {
                showError("请先选择要缴费的项目");
                return;
            }

            int feeId = (int) tableModel.getValueAt(selectedRow, 0);
            String status = (String) tableModel.getValueAt(selectedRow, 3);

            if ("PAID".equals(status)) {
                showError("此费用已缴纳");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "确定要缴纳此费用吗?",
                    "确认缴费",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (feeManager.updateFeeStatus(feeId, "PAID")) {
                        loadFees();
                        JOptionPane.showMessageDialog(this, "缴费成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        showError("缴费失败");
                    }
                } catch (SQLException ex) {
                    showError("数据库错误: " + ex.getMessage());
                }
            }
        }
    }

    class UserRepairPanel extends JPanel {
        private DefaultTableModel tableModel;

        public UserRepairPanel() throws SQLException {
            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // 工具栏
            JToolBar toolBar = new JToolBar();
            toolBar.setFloatable(false);
            JButton addBtn = new JButton("新增报修");
            JButton refreshBtn = new JButton("刷新");

            Dimension btnSize = new Dimension(100, 30);
            addBtn.setPreferredSize(btnSize);
            refreshBtn.setPreferredSize(btnSize);

            toolBar.add(addBtn);
            toolBar.addSeparator();
            toolBar.add(refreshBtn);
            add(toolBar, BorderLayout.SOUTH);

            // 表格
            tableModel = new DefaultTableModel(
                    new Object[]{"ID", "标题", "状态", "创建时间", "完成时间", "处理备注"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            JTable repairTable = new JTable(tableModel);
            repairTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(repairTable);
            add(scrollPane, BorderLayout.CENTER);

            // 加载数据
            loadRepairs();

            // 添加事件监听
            addBtn.addActionListener(e -> addRepair());
            refreshBtn.addActionListener(e -> {
                try {
                    loadRepairs();
                } catch (SQLException ex) {
                    showError("刷新失败: " + ex.getMessage());
                }
            });
        }

        private void loadRepairs() throws SQLException {
            tableModel.setRowCount(0);
            if (currentUser.getResidentId() == null) {
                showError("当前用户未关联住户信息");
                return;
            }

            List<Repair> repairs = repairManager.getRepairsByResidentId(currentUser.getResidentId());
            for (Repair repair : repairs) {
                tableModel.addRow(new Object[]{
                        repair.getId(),
                        repair.getTitle(),
                        repair.getStatus(),
                        repair.getCreatedAt(),
                        repair.getCompletedAt(),
                        repair.getStaffNotes()
                });
            }
        }

        private void addRepair() {
            if (currentUser.getResidentId() == null) {
                showError("当前用户未关联住户信息");
                return;
            }

            JTextField titleField = new JTextField();
            JTextArea descArea = new JTextArea(5, 20);
            JScrollPane descScroll = new JScrollPane(descArea);

            Object[] message = {
                    "报修标题:", titleField,
                    "详细描述:", descScroll
            };

            int option = JOptionPane.showConfirmDialog(
                    this,
                    message,
                    "新增报修",
                    JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String title = titleField.getText().trim();
                String description = descArea.getText().trim();

                if (title.isEmpty()) {
                    showError("报修标题不能为空");
                    return;
                }

                try {
                    Repair repair = new Repair();
                    repair.setResidentId(currentUser.getResidentId());
                    repair.setTitle(title);
                    repair.setDescription(description);
                    repair.setStatus("PENDING");

                    if (repairManager.addRepair(repair)) {
                        loadRepairs();
                        JOptionPane.showMessageDialog(this, "报修提交成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        showError("报修提交失败");
                    }
                } catch (SQLException ex) {
                    showError("数据库错误: " + ex.getMessage());
                }
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "错误", JOptionPane.ERROR_MESSAGE);
    }
}