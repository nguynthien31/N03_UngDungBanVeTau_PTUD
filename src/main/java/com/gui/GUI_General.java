package com.gui;

import javax.swing.*;

import com.gui.GUI_NhanVien.TAB_QLNhanVien;
import com.service.TabStyler;
import java.awt.*;

public class GUI_General extends JPanel {
	private TAB_Dashboard tab_Dashboard;
	private TAB_BanVe tab_BanVe;
	private TAB_HoanVe tab_HoanVe;
	private TAB_ThanhToanLapHD tab_ThanhToanLapHD;
	private TAB_Tau tab_Tau;
	private TAB_Toa_ChoNgoi tab_Toa_ChoNgoi;
    private TAB_Ga_Tuyen tab_Ga_Tuyen;
    private TAB_LichTrinh_ChuyenTau tab_LichTrinh_ChuyenTau;
    private TAB_Gia tab_Gia;
    private TAB_QLNhanVien tab_QLNhanVien;
    private TAB_QLKhachHang tab_QLKhachHang;
    private TAB_KhuyenMai tab_KhuyenMai;
    private TAB_ThongKeDoanhThu tab_ThongKeDoanhThu;
    private TAB_ThongKeVe tab_ThongKeVe;
    
    private JPanel contentPanel;
    private JPanel currentTabPanel;

    // Constructor khởi tạo giao diện
    public GUI_General() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Khởi tạo tab
        tab_Dashboard = new TAB_Dashboard();
        tab_BanVe = new TAB_BanVe();
        tab_HoanVe = new TAB_HoanVe();
        tab_ThanhToanLapHD = new TAB_ThanhToanLapHD();
        tab_Tau = new TAB_Tau();
        tab_Toa_ChoNgoi = new TAB_Toa_ChoNgoi();
        tab_Ga_Tuyen = new TAB_Ga_Tuyen();
        tab_LichTrinh_ChuyenTau = new TAB_LichTrinh_ChuyenTau();
        tab_Gia = new TAB_Gia();
        tab_QLNhanVien = new TAB_QLNhanVien();
        tab_QLKhachHang = new TAB_QLKhachHang();
        tab_KhuyenMai = new TAB_KhuyenMai();
        tab_ThongKeDoanhThu = new TAB_ThongKeDoanhThu();
        tab_ThongKeVe = new TAB_ThongKeVe();

        // ================= 1. SIDEBAR PANEL (NẰM TRỌN BÊN TRÁI) =================
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(0, 125, 250)); 
        sidebarPanel.setPreferredSize(new Dimension(250, 0)); // Chiều rộng Sidebar là 200px
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0, 100, 200)));

        addTabButtons(sidebarPanel);
        
        // Thêm Sidebar vào vị trí WEST của Frame tổng
        add(sidebarPanel, BorderLayout.WEST);


        // ================= 2. RIGHT PANEL (GOM HEADER VÀ CONTENT) =================
        JPanel rightPanel = new JPanel(new BorderLayout());

        // --- HEADER PANEL ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 255, 255)); 
        headerPanel.setPreferredSize(new Dimension(0, 65));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        // ===== App name =====
        JLabel lblAppName = new JLabel("Hệ thống quản lý bán vé ga tàu");
        lblAppName.setFont(TabStyler.HEADER_FONT);
        lblAppName.setForeground(Color.BLUE);

        headerPanel.add(lblAppName, BorderLayout.WEST);

        // ===== Header Right (Xin chào + Thoát) =====
        JPanel headerRight = new JPanel();
        headerRight.setOpaque(false);
        headerRight.setLayout(new BoxLayout(headerRight, BoxLayout.X_AXIS));

        JButton btnExit = new JButton("Thoát");
        btnExit.setFont(TabStyler.CONTENT_FONT);
        btnExit.setBackground(Color.WHITE);
        btnExit.setForeground(new Color(220, 53, 69));
        btnExit.setFocusPainted(false);
        btnExit.setPreferredSize(new Dimension(120, 40));
        btnExit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnExit.addActionListener(e -> handleExit());

        JLabel lblHello = new JLabel("Xin chào!");
        lblHello.setFont(TabStyler.CONTENT_FONT);
        lblHello.setForeground(Color.BLUE);

        headerRight.add(lblHello);
        headerRight.add(Box.createHorizontalStrut(30));
        headerRight.add(btnExit);

        headerPanel.add(headerRight, BorderLayout.EAST);
        
        // Đặt Header lên phía NORTH của Right Panel
        rightPanel.add(headerPanel, BorderLayout.NORTH);

        // --- CONTENT PANEL ---
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        // Mặc định hiển thị tab Dashboard sau khi đăng nhập
        showTab(tab_Dashboard);

        // Đặt Content vào giữa của Right Panel
        rightPanel.add(contentPanel, BorderLayout.CENTER);


        // ================= 3. GHÉP RIGHT PANEL VÀO FRAME TỔNG =================
        add(rightPanel, BorderLayout.CENTER);
    }
    
    private void addTabButtons(JPanel sidebarPanel) {
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebarPanel.add(createSideTitle("Quản lý"));
        
        // Chỉ add tab_Ga_Tuyen vào sidebar
        addTabButton(sidebarPanel, "Màn hình chính", "src/main/resources/icons/home.png", tab_Dashboard);
        addTabButton(sidebarPanel, "Bán vé", "src/main/resources/icons/home.png", tab_BanVe);
        addTabButton(sidebarPanel, "Hoàn vé", "src/main/resources/icons/home.png", tab_HoanVe);
        addTabButton(sidebarPanel, "Thanh toán & Lập hóa đơn", "src/main/resources/icons/home.png", tab_ThanhToanLapHD);
//        addTabButton(sidebarPanel, "Tàu", "src/main/resources/icons/home.png", tab_Tau);
//        addTabButton(sidebarPanel, "Toa & Chỗ ngồi", "src/main/resources/icons/home.png", tab_Toa_ChoNgoi);
        addDropdownMenu(sidebarPanel, "Quản lý Đoàn tàu", "src/main/resources/icons/home.png");
        addDropdownMenu(sidebarPanel, "Quản lý Lịch trình & Giá", "src/main/resources/icons/home.png");
        addTabButton(sidebarPanel, "Quản lý Nhân viên", "src/main/resources/icons/home.png", tab_QLNhanVien);
        addTabButton(sidebarPanel, "Quản lý Khách hàng", "src/main/resources/icons/home.png", tab_QLKhachHang);
        addTabButton(sidebarPanel, "Khuyến mãi", "src/main/resources/icons/home.png", tab_KhuyenMai);
        addDropdownMenu(sidebarPanel, "Thống kê", "src/main/resources/icons/home.png");

        sidebarPanel.add(Box.createVerticalGlue());
    }
    
    private JLabel createSideTitle(String title) {
        JLabel lbl = new JLabel("   " + title);
        lbl.setFont(TabStyler.SECTION_FONT);
        // ĐÃ ĐỔI MÀU Ở ĐÂY: Đổi chữ "Quản lý" thành màu trắng sáng cho dễ nhìn trên nền xanh
        lbl.setForeground(new Color(220, 230, 255)); 
        lbl.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 0));
        return lbl;
    }

    private void addTabButton(JPanel sidebarPanel, String title, String icon, JPanel targetPanel) {
        JButton button = createTabButton(title, icon);
        button.addActionListener(e -> showTab(targetPanel));
        sidebarPanel.add(button);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private JButton createTabButton(String text, String icon) {
        JButton button = new JButton();
        try {
            ImageIcon imageIcon = new ImageIcon(icon);
            Image img = imageIcon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));
            button.setText("  " + text);
        } catch (Exception ex) {
            button.setText(text); // fallback
        }

        button.setFont(TabStyler.CONTENT_FONT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(250, 50));
        button.setPreferredSize(new Dimension(250, 50));
        
        // ĐÃ ĐỔI MÀU Ở ĐÂY: Nền nút đồng màu với Sidebar, chữ màu trắng
        button.setBackground(new Color(0, 122, 255));
        button.setForeground(Color.WHITE);
        
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Hover effect (Hiệu ứng khi di chuột qua nút)
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // ĐÃ ĐỔI MÀU Ở ĐÂY: Khi đưa chuột vào, màu xanh sẽ đậm hơn một chút
                button.setBackground(new Color(0, 90, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // ĐÃ ĐỔI MÀU Ở ĐÂY: Khi đưa chuột ra, trả về màu xanh ban đầu
                button.setBackground(new Color(0, 122, 255));
            }
        });

        return button;
    }

    private void showTab(JPanel tabPanel) {
        if (currentTabPanel != null) {
            contentPanel.remove(currentTabPanel);
        }
        currentTabPanel = tabPanel;
        contentPanel.add(currentTabPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void handleExit() {
        int result = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn thoát ứng dụng?",
                "Xác nhận thoát",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    private void addDropdownMenu(JPanel sidebarPanel, String title, String icon) {
        // 1. Tạo nút cha (có thêm ký tự mũi tên để nhận diện)
        JButton parentButton = createTabButton("▼ " + title, icon);

        // 2. Tạo Panel chứa các menu con
        JPanel subMenuPanel = new JPanel();
        subMenuPanel.setLayout(new BoxLayout(subMenuPanel, BoxLayout.Y_AXIS));
        subMenuPanel.setBackground(new Color(0, 125, 250)); // Cùng màu nền sidebar
        subMenuPanel.setVisible(false); // Ẩn đi theo mặc định

        // 3. Tạo các nút con và gán sự kiện chuyển Tab
        
        if (title.equals("Quản lý Đoàn tàu")) {
        	JButton btnTau = createSubTabButton("Tàu");
            btnTau.addActionListener(e -> showTab(tab_Tau));
            subMenuPanel.add(btnTau);
            
            JButton btnToa_CN = createSubTabButton("Toa & Chỗ ngồi");
            btnToa_CN.addActionListener(e -> showTab(tab_Toa_ChoNgoi));
            subMenuPanel.add(btnToa_CN);
        }else if (title.equals("Quản lý Lịch trình & Giá")) {
        	JButton btnGa_Tuyen = createSubTabButton("Ga & Tuyến");
            btnGa_Tuyen.addActionListener(e -> showTab(tab_Ga_Tuyen));
            subMenuPanel.add(btnGa_Tuyen);
            
            JButton btnLT_CT = createSubTabButton("Lịch trình & Chuyến tàu");
            btnLT_CT.addActionListener(e -> showTab(tab_LichTrinh_ChuyenTau));
            subMenuPanel.add(btnLT_CT);
            
            JButton btnGia = createSubTabButton("Giá");
            btnGia.addActionListener(e -> showTab(tab_Gia));
            subMenuPanel.add(btnGia);
        }else if (title.equals("Thống kê")) {
        	JButton btnDoanhThu = createSubTabButton("Thống kê doanh thu");
            btnDoanhThu.addActionListener(e -> showTab(tab_ThongKeDoanhThu));
            subMenuPanel.add(btnDoanhThu);

            JButton btnVe = createSubTabButton("Thống kê vé");
            btnVe.addActionListener(e -> showTab(tab_ThongKeVe));
            subMenuPanel.add(btnVe);
        }

        // 4. Bắt sự kiện click cho nút cha để Đóng/Mở subMenuPanel
        parentButton.addActionListener(e -> {
            boolean isVisible = subMenuPanel.isVisible();
            subMenuPanel.setVisible(!isVisible); // Đảo ngược trạng thái
            
            // Cập nhật lại giao diện Sidebar
            sidebarPanel.revalidate();
            sidebarPanel.repaint();
        });

        // 5. Add tất cả vào Sidebar
        sidebarPanel.add(parentButton);
        sidebarPanel.add(subMenuPanel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    // Hàm hỗ trợ tạo giao diện cho nút con (sub-menu)
    private JButton createSubTabButton(String text) {
        // Thụt lề text vào trong một chút để tạo cảm giác cấp bậc (hierarchy)
        JButton button = new JButton(text);
        button.setFont(TabStyler.CONTENT_FONT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(250, 40));
        button.setPreferredSize(new Dimension(250, 40));
        
        // Nền nút con sẫm màu hơn nút cha một chút để dễ phân biệt
        button.setBackground(new Color(0, 105, 220)); 
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        // Hiệu ứng Hover cho nút con
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 85, 190));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 105, 220));
            }
        });

        return button;
    }

}