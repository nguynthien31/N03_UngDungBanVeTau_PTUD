package com.gui;

import com.entities.NhanVien;
import com.service.TabStyler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GUI_Login extends JPanel {

    private JFrame parentFrame;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public GUI_Login() {

        setLayout(new BorderLayout());

        // ================= LEFT IMAGE =================
        JPanel left = new JPanel(new BorderLayout());
        left.setPreferredSize(new Dimension(520, 600));
        left.setBackground(new Color(245, 247, 255));

        try {
            ImageIcon icon = new ImageIcon("src/main/resources/login_img.png");
            Image img = icon.getImage().getScaledInstance(420, 420, Image.SCALE_SMOOTH);
            JLabel lbImg = new JLabel(new ImageIcon(img));
            lbImg.setHorizontalAlignment(SwingConstants.CENTER);
            lbImg.setBorder(new EmptyBorder(40, 0, 40, 0));
            left.add(lbImg, BorderLayout.CENTER);
        } catch (Exception e) {
            left.add(new JLabel("Image not found", SwingConstants.CENTER), BorderLayout.CENTER);
        }

        // ================= RIGHT FORM =================
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(Color.WHITE);
        right.setBorder(new EmptyBorder(40, 50, 40, 50));

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.insets = new Insets(10, 0, 10, 0);
        gc.anchor = GridBagConstraints.WEST;

        JLabel lblTitle = new JLabel("Đăng nhập");
        lblTitle.setFont(TabStyler.HEADER_FONT);

        JLabel lblUser = new JLabel("Username (Bỏ qua)");
        txtUsername = new JTextField(22);

        JLabel lblPass = new JLabel("Password (Bỏ qua)");
        txtPassword = new JPasswordField(22);

        // Enter vào ô pass cũng kích hoạt nút login
        txtPassword.addActionListener(e -> btnLogin.doClick());

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(TabStyler.SECTION_FONT);
        btnLogin.setBackground(new Color(0, 122, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(200, 38));
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // CHỈNH SỬA Ở ĐÂY: Bấm nút là gọi thẳng hàm mở cửa sổ mới
        btnLogin.addActionListener(e -> handleLoginBypass());

        gc.gridy = 0;    right.add(lblTitle, gc);
        gc.gridy++;      right.add(lblUser, gc);
        gc.gridy++;      right.add(txtUsername, gc);
        gc.gridy++;      right.add(lblPass, gc);
        gc.gridy++;      right.add(txtPassword, gc);

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setBackground(Color.WHITE);
        row.add(btnLogin);

        gc.gridy++;
        right.add(row, gc);

        add(left, BorderLayout.WEST);
        add(right, BorderLayout.EAST);
    }

    public void setParentFrame(JFrame f) {
        this.parentFrame = f;
    }

 // Hàm login bỏ qua mọi bước kiểm tra database
    private void handleLoginBypass() {
        // Gửi thông báo nhỏ cho vui
        JOptionPane.showMessageDialog(this,
                "Đã bỏ qua đăng nhập. Đang vào hệ thống...",
                "Chế độ Test", JOptionPane.INFORMATION_MESSAGE);

        // Gọi hàm mở cửa sổ (Không cần truyền nv vào nữa)
        openMainWindow();
    }

    // Đã xóa tham số (NhanVien nv) ở đây
    private void openMainWindow() {
        if (parentFrame == null) {
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w instanceof JFrame) parentFrame = (JFrame) w;
        }

        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Hệ thống quản lý bán vé ga tàu");
            
            // ĐÃ SỬA Ở ĐÂY: Xóa chữ nv đi, chỉ còn GUI_General()
            f.setContentPane(new GUI_General()); 
            
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full màn hình
            f.setResizable(true); 
            f.setVisible(true);

            // Tắt cửa sổ Login cũ đi
            if (parentFrame != null) {
                parentFrame.dispose();
            }
        });
    }

    // ==========================================
    // Hàm Main để bạn chạy thử giao diện Login này
    // ==========================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("KVStore - Đăng nhập");
            GUI_Login loginPanel = new GUI_Login();
            loginPanel.setParentFrame(frame);
            frame.setContentPane(loginPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }
}