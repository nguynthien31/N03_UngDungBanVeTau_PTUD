package com.gui;

import javax.swing.*;
import java.awt.*;

public class TAB_Ga_Tuyen extends JPanel {
    
    // Constructor
    public TAB_Ga_Tuyen() {
        // Setup bố cục và màu nền 
        setLayout(new BorderLayout());
        setBackground(Color.WHITE); 

        // === THÊM THỬ NỘI DUNG ĐỂ KIỂM TRA ===
        
        // 1. Một dòng chữ tiêu đề ở trên cùng
        JLabel lblTitle = new JLabel("GA & TUYẾN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 122, 255));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Tạo khoảng trống trên dưới
        
        add(lblTitle, BorderLayout.NORTH);

    }
}