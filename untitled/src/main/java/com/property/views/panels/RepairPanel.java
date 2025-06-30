package com.property.views;

import com.property.managers.RepairManager;
import com.property.managers.ResidentManager;
import com.property.models.Repair;
import com.property.models.Resident;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class RepairPanel extends JPanel {
    private RepairManager repairManager;
    private ResidentManager residentManager;
    private JTable repairTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusCombo;

    public RepairPanel() throws SQLException {
        repairManager = new RepairManager();
        residentManager = new ResidentManager();
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 筛选面板
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusCombo = new JComboBox<>(new String[]{"所有状态", "PENDING", "PROCESSING", "COMPLETED"});
        JButton filterBtn = new JButton("筛选");
        filterBtn.setPreferredSize(new Dimension(80, 30));

        filterPanel.add(new JLabel("状态:"));
        filterPanel.add(statusCombo);
        filterPanel.add(filterBtn);
        add(filterPanel, BorderLayout.NORTH);

        // 工具栏
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton processBtn = new JButton("开始处理");
        JButton completeBtn = new JButton("标记完成");
        JButton refreshBtn = new JButton("刷新");

        Dimension btnSize = new Dimension(100, 30);
        processBtn.setPreferredSize(btnSize);
        completeBtn.setPreferredSize(btnSize);
        refreshBtn.setPreferredSize(btnSize);

        toolBar.add(processBtn);
        toolBar.add(completeBtn);
        toolBar.addSeparator();
        toolBar.add(refreshBtn);
        add(toolBar, BorderLayout.SOUTH);

        // 表格
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "住户ID", "住户姓名", "标题", "状态", "创建时间", "完成时间"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        repairTable = new JTable(tableModel);
        repairTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(repairTable);
        add(scrollPane, BorderLayout.CENTER);

        // 加载数据
        loadRepairs();

        // 添加事件监听
        filterBtn.addActionListener(e -> filterRepairs());
        processBtn.addActionListener(this::processRepair);
        completeBtn.addActionListener(this::completeRepair);
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
        List<Repair> repairs = repairManager.getRepairsByStatus("PENDING");
        addRepairsToTable(repairs);
    }

    private void filterRepairs() {
        try {
            tableModel.setRowCount(0);
            String selectedStatus = statusCombo.getSelectedIndex() == 0 ?
                    null : (String) statusCombo.getSelectedItem();

            List<Repair> repairs;
            if (selectedStatus == null) {
                repairs = repairManager.getRepairsByStatus("PENDING");
                repairs.addAll(repairManager.getRepairsByStatus("PROCESSING"));
                repairs.addAll(repairManager.getRepairsByStatus("COMPLETED"));
            } else {
                repairs = repairManager.getRepairsByStatus(selectedStatus);
            }

            addRepairsToTable(repairs);
        } catch (SQLException ex) {
            showError("筛选失败: " + ex.getMessage());
        }
    }

    private void addRepairsToTable(List<Repair> repairs) throws SQLException {
        for (Repair repair : repairs) {
            Resident resident = residentManager.getResidentById(repair.getResidentId());
            String residentName = resident != null ? resident.getName() : "未知";
            tableModel.addRow(new Object[]{
                    repair.getId(),
                    repair.getResidentId(),
                    residentName,
                    repair.getTitle(),
                    repair.getStatus(),
                    repair.getCreatedAt(),
                    repair.getCompletedAt()
            });
        }
    }

    private void processRepair(ActionEvent e) {
        int selectedRow = repairTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("请先选择要处理的报修");
            return;
        }

        int repairId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 4);

        if ("PROCESSING".equals(currentStatus)) {
            showError("此报修已经在处理中");
            return;
        }

        if ("COMPLETED".equals(currentStatus)) {
            showError("此报修已完成，无法再次处理");
            return;
        }

        JTextArea notesArea = new JTextArea(5, 30);
        JScrollPane notesScroll = new JScrollPane(notesArea);

        int option = JOptionPane.showConfirmDialog(
                this,
                new Object[]{"处理备注:", notesScroll},
                "处理报修",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                if (repairManager.updateRepairStatus(repairId, "PROCESSING", notesArea.getText())) {
                    loadRepairs();
                    JOptionPane.showMessageDialog(this, "报修状态更新成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showError("更新报修状态失败");
                }
            } catch (SQLException ex) {
                showError("数据库错误: " + ex.getMessage());
            }
        }
    }

    private void completeRepair(ActionEvent e) {
        int selectedRow = repairTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("请先选择要完成的报修");
            return;
        }

        int repairId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 4);

        if ("COMPLETED".equals(currentStatus)) {
            showError("此报修已完成");
            return;
        }

        if ("PENDING".equals(currentStatus)) {
            showError("请先处理此报修");
            return;
        }

        JTextArea notesArea = new JTextArea(5, 30);
        JScrollPane notesScroll = new JScrollPane(notesArea);

        int option = JOptionPane.showConfirmDialog(
                this,
                new Object[]{"完成备注:", notesScroll},
                "完成报修",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                if (repairManager.updateRepairStatus(repairId, "COMPLETED", notesArea.getText())) {
                    loadRepairs();
                    JOptionPane.showMessageDialog(this, "报修已完成", "成功", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showError("更新报修状态失败");
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