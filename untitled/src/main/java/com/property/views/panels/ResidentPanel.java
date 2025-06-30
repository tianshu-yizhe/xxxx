package com.property.views;

import com.property.managers.ResidentManager;
import com.property.models.Resident;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class ResidentPanel extends JPanel {
    private ResidentManager residentManager;
    private JTable residentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public ResidentPanel() throws SQLException {
        residentManager = new ResidentManager();
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
        JButton editBtn = new JButton("编辑");
        JButton deleteBtn = new JButton("删除");
        JButton refreshBtn = new JButton("刷新");

        // 设置按钮大小一致
        Dimension btnSize = new Dimension(80, 30);
        addBtn.setPreferredSize(btnSize);
        editBtn.setPreferredSize(btnSize);
        deleteBtn.setPreferredSize(btnSize);
        refreshBtn.setPreferredSize(btnSize);

        toolBar.add(addBtn);
        toolBar.add(editBtn);
        toolBar.add(deleteBtn);
        toolBar.addSeparator();
        toolBar.add(refreshBtn);
        add(toolBar, BorderLayout.SOUTH);

        // 表格
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "姓名", "电话", "房间号", "身份证号", "创建时间"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };
        residentTable = new JTable(tableModel);
        residentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        residentTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(residentTable);
        add(scrollPane, BorderLayout.CENTER);

        // 加载数据
        loadResidents();

        // 添加事件监听
        searchBtn.addActionListener(e -> searchResidents());
        addBtn.addActionListener(this::addResident);
        editBtn.addActionListener(this::editResident);
        deleteBtn.addActionListener(this::deleteResident);
        refreshBtn.addActionListener(e -> {
            try {
                loadResidents();
            } catch (SQLException ex) {
                showError("刷新失败: " + ex.getMessage());
            }
        });
    }

    private void loadResidents() throws SQLException {
        tableModel.setRowCount(0);
        List<Resident> residents = residentManager.getAllResidents();
        for (Resident resident : residents) {
            tableModel.addRow(new Object[]{
                    resident.getId(),
                    resident.getName(),
                    resident.getPhone(),
                    resident.getRoomNumber(),
                    resident.getIdentityNumber(),
                    resident.getCreatedAt()
            });
        }
    }

    private void searchResidents() {
        try {
            String keyword = searchField.getText().trim();
            tableModel.setRowCount(0);
            List<Resident> residents = residentManager.searchResidents(keyword);
            for (Resident resident : residents) {
                tableModel.addRow(new Object[]{
                        resident.getId(),
                        resident.getName(),
                        resident.getPhone(),
                        resident.getRoomNumber(),
                        resident.getIdentityNumber(),
                        resident.getCreatedAt()
                });
            }
        } catch (SQLException ex) {
            showError("搜索失败: " + ex.getMessage());
        }
    }

    private void addResident(ActionEvent e) {
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField roomField = new JTextField();
        JTextField idField = new JTextField();

        Object[] message = {
                "姓名:", nameField,
                "电话:", phoneField,
                "房间号:", roomField,
                "身份证号:", idField
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                "添加新住户",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                Resident resident = new Resident();
                resident.setName(nameField.getText().trim());
                resident.setPhone(phoneField.getText().trim());
                resident.setRoomNumber(roomField.getText().trim());
                resident.setIdentityNumber(idField.getText().trim());

                if (resident.getName().isEmpty() || resident.getPhone().isEmpty() ||
                        resident.getRoomNumber().isEmpty() || resident.getIdentityNumber().isEmpty()) {
                    showError("所有字段都必须填写");
                    return;
                }

                if (residentManager.addResident(resident)) {
                    loadResidents();
                    JOptionPane.showMessageDialog(this, "住户添加成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showError("添加住户失败");
                }
            } catch (SQLException ex) {
                showError("数据库错误: " + ex.getMessage());
            }
        }
    }

    private void editResident(ActionEvent e) {
        int selectedRow = residentTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("请先选择要编辑的住户");
            return;
        }

        int residentId = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            Resident resident = residentManager.getResidentById(residentId);
            if (resident == null) {
                showError("找不到选中的住户");
                return;
            }

            JTextField nameField = new JTextField(resident.getName());
            JTextField phoneField = new JTextField(resident.getPhone());
            JTextField roomField = new JTextField(resident.getRoomNumber());
            JTextField idField = new JTextField(resident.getIdentityNumber());

            Object[] message = {
                    "姓名:", nameField,
                    "电话:", phoneField,
                    "房间号:", roomField,
                    "身份证号:", idField
            };

            int option = JOptionPane.showConfirmDialog(
                    this,
                    message,
                    "编辑住户信息",
                    JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                resident.setName(nameField.getText().trim());
                resident.setPhone(phoneField.getText().trim());
                resident.setRoomNumber(roomField.getText().trim());
                resident.setIdentityNumber(idField.getText().trim());

                if (resident.getName().isEmpty() || resident.getPhone().isEmpty() ||
                        resident.getRoomNumber().isEmpty() || resident.getIdentityNumber().isEmpty()) {
                    showError("所有字段都必须填写");
                    return;
                }

                if (residentManager.updateResident(resident)) {
                    loadResidents();
                    JOptionPane.showMessageDialog(this, "住户信息更新成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showError("更新住户信息失败");
                }
            }
        } catch (SQLException ex) {
            showError("数据库错误: " + ex.getMessage());
        }
    }

    private void deleteResident(ActionEvent e) {
        int selectedRow = residentTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("请先选择要删除的住户");
            return;
        }

        int residentId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "确定要删除此住户吗?",
                "确认删除",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (residentManager.deleteResident(residentId)) {
                    loadResidents();
                    JOptionPane.showMessageDialog(this, "住户删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showError("删除住户失败");
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