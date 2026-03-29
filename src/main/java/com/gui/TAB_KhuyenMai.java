package com.gui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class TAB_KhuyenMai extends JPanel{
	// Constructor
    public TAB_KhuyenMai() {
        // Setup bố cục và màu nền 
        setLayout(new BorderLayout());
        setBackground(Color.WHITE); 

        // === THÊM THỬ NỘI DUNG ĐỂ KIỂM TRA ===
        
        // 1. Một dòng chữ tiêu đề ở trên cùng
        JLabel lblTitle = new JLabel("KHUYẾN MÃI", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 122, 255));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Tạo khoảng trống trên dưới
        
        add(lblTitle, BorderLayout.NORTH);
        
        add(createHeader(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
    }
 // ===== HEADER =====
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel lblTitle = new JLabel("Quản lý khuyến mãi", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 122, 255));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Tạo khoảng trống trên dưới

        JButton addBtn = new JButton("+ Thêm Khuyến Mãi");

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(addBtn, BorderLayout.EAST);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return panel;
    }

    // ===== MAIN =====
    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));

        panel.add(createStatsPanel(), BorderLayout.NORTH);
        panel.add(createCenterPanel(), BorderLayout.CENTER);

        return panel;
    }

    // ===== CARD STATS =====
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        panel.add(createCard("5", "Tổng Chương Trình"));
        panel.add(createCard("3", "Đang Hoạt Động"));

        return panel;
    }

    private JPanel createCard(String number, String label) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel num = new JLabel(number);
        num.setFont(new Font("Arial", Font.BOLD, 22));
        num.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel text = new JLabel(label);
        text.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.add(num);
        pnl.add(text);

        card.add(pnl, BorderLayout.CENTER);
        return card;
    }

    // ===== CENTER =====
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        panel.add(createFilterPanel(), BorderLayout.NORTH);
        panel.add(createTable(), BorderLayout.CENTER);

        return panel;
    }

    // ===== FILTER =====
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JTextField search = new JTextField(25);
        search.setText("Tìm theo tên chương trình...");

        JComboBox<String> status = new JComboBox<>(new String[]{
                "Tất cả trạng thái", "ACTIVE", "UPCOMING"
        });

        JButton filterBtn = new JButton("Lọc");

        panel.add(search);
        panel.add(status);
        panel.add(filterBtn);

        return panel;
    }

    // ===== TABLE =====
    private JScrollPane createTable() {
        String[] columns = {
                "#", "Tên CT", "Mã KM", "Loại", "Mức giảm",
                "Hiệu lực", "Đã dùng", "Trạng thái"
        };

        Object[][] data = {
                {"1", "Giảm giá mùa hè", "GIAM20PT", "%", "20%",
                        "13/03 - 23/06", "9", "ACTIVE"},
                {"2", "Chương trình Tết", "TET2026", "VNĐ", "50,000",
                        "13/03 - 23/06", "26", "UPCOMING"},
                {"3", "Khuyến mãi tuần", "TUAN70", "%", "70%",
                        "13/03 - 23/06", "1", "ACTIVE"},
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable table = new JTable(model);

        // ===== CUSTOM STATUS COLOR =====
        table.getColumn("Trạng thái").setCellRenderer(new StatusRenderer());

        table.setRowHeight(30);

        return new JScrollPane(table);
    }

    // ===== STATUS COLOR =====
    class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row, int column) {

            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            String status = value.toString();

            if (status.equals("ACTIVE")) {
                label.setForeground(new Color(0, 128, 0));
            } else {
                label.setForeground(Color.ORANGE);
            }

            return label;
        }
    }
    
}
