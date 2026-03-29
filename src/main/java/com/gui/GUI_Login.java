package com.gui;

import com.service.TabStyler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class GUI_Login extends JPanel implements ActionListener, MouseListener {

    private JFrame parentFrame;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    // ===== THÊM LABEL LỖI =====
    private JLabel lblUserError;
    private JLabel lblPassError;

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
        gc.insets = new Insets(5, 0, 5, 0);
        gc.anchor = GridBagConstraints.WEST;

        JLabel lblTitle = new JLabel("Đăng nhập");
        lblTitle.setFont(TabStyler.HEADER_FONT);

        JLabel lblUser = new JLabel("Username");
        txtUsername = new JTextField(22);

        JLabel lblPass = new JLabel("Password");
        txtPassword = new JPasswordField(22);

        // ===== LABEL LỖI =====
        lblUserError = new JLabel(" ");
        lblUserError.setForeground(Color.RED);
        lblUserError.setFont(new Font("Arial", Font.ITALIC, 12));

        lblPassError = new JLabel(" ");
        lblPassError.setForeground(Color.RED);
        lblPassError.setFont(new Font("Arial", Font.ITALIC, 12));

        // Enter = login
        txtPassword.addActionListener(e -> btnLogin.doClick());

        // Clear lỗi khi nhập lại
        txtUsername.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                lblUserError.setText(" ");
            }
        });

        txtPassword.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                lblPassError.setText(" ");
            }
        });

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(TabStyler.SECTION_FONT);
        btnLogin.setBackground(new Color(0, 122, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(200, 38));
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnLogin.addActionListener(e -> handleLogin());

        // ===== ADD COMPONENT =====
        gc.gridy = 0;    right.add(lblTitle, gc);
        gc.gridy++;      right.add(lblUser, gc);
        gc.gridy++;      right.add(txtUsername, gc);
        gc.gridy++;      right.add(lblUserError, gc);
        gc.gridy++;      right.add(lblPass, gc);
        gc.gridy++;      right.add(txtPassword, gc);
        gc.gridy++;      right.add(lblPassError, gc);

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

    // ===== HÀM LOGIN CHÍNH =====
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        boolean isValid = true;

        // reset lỗi
        lblUserError.setText(" ");
        lblPassError.setText(" ");

        // check username
        if (username.isEmpty()) {
            lblUserError.setText("Username không được để trống");
            isValid = false;
        } else if (!username.equals("admin")) {
            lblUserError.setText("Sai username");
            isValid = false;
        }

        // check password
        if (password.isEmpty()) {
            lblPassError.setText("Password không được để trống");
            isValid = false;
        } else if (!password.equals("123")) {
            lblPassError.setText("Sai password");
            isValid = false;
        }

        if (!isValid) return;

        // đúng thì vào hệ thống
        openMainWindow();
    }

    private void openMainWindow() {
        if (parentFrame == null) {
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w instanceof JFrame) parentFrame = (JFrame) w;
        }

        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Hệ thống quản lý bán vé ga tàu");
            f.setContentPane(new GUI_General());
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setExtendedState(JFrame.MAXIMIZED_BOTH);
            f.setVisible(true);

            if (parentFrame != null) {
                parentFrame.dispose();
            }
        });
    }

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

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void actionPerformed(ActionEvent e) {}
}