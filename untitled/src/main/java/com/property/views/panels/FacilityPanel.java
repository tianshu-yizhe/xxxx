package com.property.views;

import com.property.managers.FacilityManager;
import com.property.models.Facility;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class FacilityPanel extends JPanel {
    private FacilityManager facilityManager;
    private JTable facilityTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public FacilityPanel() throws SQLException {
        facilityManager = new FacilityManager();
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(25);
        JButton searchBtn = new JButton("搜索");
        searchBtn.setPreferredSize(new Dimension(80, 30));
        searchPanel.add(new JLabel("搜索:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        add(searchPanel, BorderLayout.NORTH);

        // 工具栏
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton addBtn = new JButton("添加");
        JButton updateBtn = new JButton("更新状态");
        JButton deleteBtn = new JButton("删除");
        JButton refreshBtn = new JButton("刷新");

        Dimension btnSize = new Dimension(80, 30);
        addBtn.setPreferredSize(btnSize);
        updateBtn.setPreferredSize(btnSize);
        deleteBtn.setPreferredSize(btnSize);
        refreshBtn.setPreferredSize(btnSize);

        toolBar.add(addBtn);
        toolBar.add(updateBtn);
        toolBar.add(deleteBtn);
        toolBar.addSeparator();
        toolBar.add(refreshBtn);
        add(toolBar, BorderLayout.SOUTH);

        // 表格
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "名称", "描述", "状态", "位置", "创建时间"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        facilityTable = new JTable(tableModel);
        facilityTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(facilityTable);
        add(scrollPane, BorderLayout.CENTER);

        // 加载数据
        loadFacilities();

        // 添加事件监听
        searchBtn.addActionListener(e -> searchFacilities());
        addBtn.addActionListener(this::addFacility);
        updateBtn.addActionListener(this::updateFacilityStatus);
        deleteBtn.addActionListener(this::deleteFacility);
        refreshBtn.addActionListener(e -> {
            try {
                loadFacilities();
            } catch (SQLException ex) {
                showError("刷新失败: " + ex.getMessage());
            }
        });
    }

    private void loadFacilities() throws SQLException {
        tableModel.setRowCount(0);
        List<Facility> facilities = facilityManager.getAllFacilities();
        for (Facility facility : facilities) {
            tableModel.addRow(new Object[]{
                    facility.getId(),
                    facility.getName(),
                    facility.getDescription(),
                    facility.getStatus(),
                    facility.getLocation(),
                    facility.getCreatedAt()
            });
        }
    }

    private void searchFacilities() {
        String keyword = searchField.getText().trim();
        tableModel.setRowCount(0);
        try {
            List<Facility> facilities = facilityManager.getAllFacilities();
            for (Facility facility : facilities) {
                if (facility.getName().contains(keyword) ||
                        facility.getDescription().contains(keyword) ||
                        facility.getLocation().contains(keyword)) {
                    tableModel.addRow(new Object[]{
                            facility.getId(),
                            facility.getName(),
                            facility.getDescription(),
                            facility.getStatus(),
                            facility.getLocation(),
                            facility.getCreatedAt()
                    });
                }
            }
        } catch (SQLException ex) {
            showError("搜索失败: " + ex.getMessage());
        }
    }

    private void addFacility(ActionEvent e) {
        JTextField nameField = new JTextField();
        JTextArea descField = new JTextArea(3, 20);
        JScrollPane descScroll = new JScrollPane(descField);
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"AVAILABLE", "MAINTENANCE", "OUT_OF_SERVICE"});
        JTextField locationField = new JTextField();

        Object[] message = {
                "设施名称:", nameField,
                "描述:", descScroll,
                "状态:", statusCombo,
                "位置:", locationField
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                "添加新设施",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                Facility facility = new Facility();
                facility.setName(nameField.getText().trim());
                facility.setDescription(descField.getText().trim());
                facility.setStatus((String) statusCombo.getSelectedItem());
                facility.setLocation(locationField.getText().trim());

                if (facility.getName().isEmpty() || facility.getLocation().isEmpty()) {
                    showError("名称和位置必须填写");
                    return;
                }

                if (facilityManager.addFacility(facility)) {
                    loadFacilities();
                    JOptionPane.showMessageDialog(this, "设施添加成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showError("添加设施失败");
                }
            } catch (SQLException ex) {
                showError("数据库错误: " + ex.getMessage());
            }
        }
    }

    private void updateFacilityStatus(ActionEvent e) {
        int selectedRow = facilityTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("请先选择要更新的设施");
            return;
        }

        int facilityId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 3);

        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"AVAILABLE", "MAINTENANCE", "OUT_OF_SERVICE"});
        statusCombo.setSelectedItem(currentStatus);

        int option = JOptionPane.showConfirmDialog(
                this,
                new Object[]{"选择新状态:", statusCombo},
                "更新设施状态",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String newStatus = (String) statusCombo.getSelectedItem();
                if (facilityManager.updateFacilityStatus(facilityId, newStatus)) {
                    loadFacilities();
                    JOptionPane.showMessageDialog(this, "状态更新成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showError("更新状态失败");
                }
            } catch (SQLException ex) {
                showError("数据库错误: " + ex.getMessage());
            }
        }
    }

    private void deleteFacility(ActionEvent e) {
        int selectedRow = facilityTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("请先选择要删除的设施");
            return;
        }

        int facilityId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "确定要删除此设施吗?",
                "确认删除",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (facilityManager.deleteFacility(facilityId)) {
                    loadFacilities();
                    JOptionPane.showMessageDialog(this, "设施删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showError("删除设施失败");
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