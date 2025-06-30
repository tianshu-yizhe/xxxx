package com.property.views;

import com.property.managers.FeeManager;
import com.property.managers.ResidentManager;
import com.property.models.Fee;
import com.property.models.Resident;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class FeePanel extends JPanel {
    private FeeManager feeManager;
    private ResidentManager residentManager;
    private JTable feeTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> residentCombo;
    private JComboBox<String> statusCombo;

    public FeePanel() throws SQLException {
        feeManager = new FeeManager();
        residentManager = new ResidentManager();
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 筛选面板
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        residentCombo = new JComboBox<>();
        residentCombo.addItem("所有住户");
        statusCombo = new JComboBox<>(new String[]{"所有状态", "UNPAID", "PAID", "OVERDUE"});
        JButton filterBtn = new JButton("筛选");
        filterBtn.setPreferredSize(new Dimension(80, 30));

        try {
            List<Resident> residents = residentManager.getAllResidents();
            for (Resident resident : residents) {
                residentCombo.addItem(resident.getName() + " (" + resident.getRoomNumber() + ")");
            }
        } catch (SQLException ex) {
            showError("加载住户列表失败: " + ex.getMessage());
        }

        filterPanel.add(new JLabel("住户:"));
        filterPanel.add(residentCombo);
        filterPanel.add(new JLabel("状态:"));
        filterPanel.add(statusCombo);
        filterPanel.add(filterBtn);
        add(filterPanel, BorderLayout.NORTH);

        // 工具栏
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton addBtn = new JButton("添加");
        JButton payBtn = new JButton("标记为已支付");
        JButton refreshBtn = new JButton("刷新");

        Dimension btnSize = new Dimension(100, 30);
        addBtn.setPreferredSize(btnSize);
        payBtn.setPreferredSize(btnSize);
        refreshBtn.setPreferredSize(btnSize);

        toolBar.add(addBtn);
        toolBar.add(payBtn);
        toolBar.addSeparator();
        toolBar.add(refreshBtn);
        add(toolBar, BorderLayout.SOUTH);

        // 表格
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "住户ID", "住户姓名", "类型", "金额", "到期日", "状态", "创建时间"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        feeTable = new JTable(tableModel);
        feeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(feeTable);
        add(scrollPane, BorderLayout.CENTER);

        // 加载数据
        loadFees();

        // 添加事件监听
        filterBtn.addActionListener(e -> filterFees());
        addBtn.addActionListener(this::addFee);
        payBtn.addActionListener(this::markAsPaid);
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
        List<Fee> fees = feeManager.getUnpaidFees();
        for (Fee fee : fees) {
            Resident resident = residentManager.getResidentById(fee.getResidentId());
            String residentName = resident != null ? resident.getName() : "未知";
            tableModel.addRow(new Object[]{
                    fee.getId(),
                    fee.getResidentId(),
                    residentName,
                    fee.getType(),
                    fee.getAmount(),
                    fee.getDueDate(),
                    fee.getStatus(),
                    fee.getCreatedAt()
            });
        }
    }

    private void filterFees() {
        try {
            tableModel.setRowCount(0);
            List<Fee> fees = feeManager.getUnpaidFees();

            int selectedResidentIndex = residentCombo.getSelectedIndex();
            String selectedStatus = statusCombo.getSelectedIndex() == 0 ?
                    null : (String) statusCombo.getSelectedItem();

            for (Fee fee : fees) {
                Resident resident = residentManager.getResidentById(fee.getResidentId());
                String residentName = resident != null ? resident.getName() : "未知";

                // 筛选住户
                boolean residentMatch = selectedResidentIndex == 0 ||
                        (resident != null && residentCombo.getSelectedItem().toString()
                                .contains(resident.getRoomNumber()));

                // 筛选状态
                boolean statusMatch = selectedStatus == null ||
                        fee.getStatus().equals(selectedStatus);

                if (residentMatch && statusMatch) {
                    tableModel.addRow(new Object[]{
                            fee.getId(),
                            fee.getResidentId(),
                            residentName,
                            fee.getType(),
                            fee.getAmount(),
                            fee.getDueDate(),
                            fee.getStatus(),
                            fee.getCreatedAt()
                    });
                }
            }
        } catch (SQLException ex) {
            showError("筛选失败: " + ex.getMessage());
        }
    }

    private void addFee(ActionEvent e) {
        try {
            List<Resident> residents = residentManager.getAllResidents();
            if (residents.isEmpty()) {
                showError("没有可用的住户，请先添加住户");
                return;
            }

            JComboBox<Resident> residentCombo = new JComboBox<>();
            for (Resident resident : residents) {
                residentCombo.addItem(resident);
            }

            JComboBox<String> typeCombo = new JComboBox<>(
                    new String[]{"PROPERTY", "WATER", "ELECTRICITY", "PARKING", "OTHER"});

            JTextField amountField = new JTextField();
            JTextField dueDateField = new JTextField();

            Object[] message = {
                    "住户:", residentCombo,
                    "费用类型:", typeCombo,
                    "金额:", amountField,
                    "到期日(YYYY-MM-DD):", dueDateField
            };

            int option = JOptionPane.showConfirmDialog(
                    this,
                    message,
                    "添加新费用",
                    JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                try {
                    Fee fee = new Fee();
                    fee.setResidentId(((Resident)residentCombo.getSelectedItem()).getId());
                    fee.setType((String) typeCombo.getSelectedItem());
                    fee.setAmount(Double.parseDouble(amountField.getText().trim()));
                    fee.setDueDate(java.sql.Date.valueOf(dueDateField.getText().trim()));
                    fee.setStatus("UNPAID");

                    if (feeManager.addFee(fee)) {
                        loadFees();
                        JOptionPane.showMessageDialog(this, "费用添加成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        showError("添加费用失败");
                    }
                } catch (IllegalArgumentException ex) {
                    showError("请输入有效的金额和日期(YYYY-MM-DD)");
                }
            }
        } catch (SQLException ex) {
            showError("数据库错误: " + ex.getMessage());
        }
    }

    private void markAsPaid(ActionEvent e) {
        int selectedRow = feeTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("请先选择要标记的费用");
            return;
        }

        int feeId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "确定要将此费用标记为已支付吗?",
                "确认支付",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (feeManager.updateFeeStatus(feeId, "PAID")) {
                    loadFees();
                    JOptionPane.showMessageDialog(this, "费用状态更新成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showError("更新费用状态失败");
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