package com.gui;

import com.dao.DAO_Ga;
import com.entities.Ga;
import com.toedter.calendar.JDateChooser; // Thư viện JCalendar
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Date;
import java.util.List;

public class TAB_Dashboard extends JPanel {
    private Color primaryColor = new Color(0, 122, 255);
    private Image backgroundImage;
    
    // Các thành phần nhập liệu
    private JComboBox<String> cbDepGa, cbDesGa;
    private JDateChooser dateDep, dateReturn;
    private JPanel pnlReturnWrapper; 
    private JRadioButton rdOneWay, rdRoundTrip;
    private JButton btnSearch;

    public TAB_Dashboard() {
        // 1. Tải ảnh nền
        try {
            backgroundImage = new ImageIcon("src/main/img/bachgrough.png").getImage();
        } catch (Exception e) {
            System.err.println("Lỗi: Không tìm thấy ảnh nền!");
        }

        setLayout(new BorderLayout());
        setBackground(new Color(242, 245, 250));

        // --- 2. PHẦN SEARCH ENGINE (NORTH) ---
        initSearchEngine();

        // --- 3. PHẦN CHUYẾN TÀU PHỔ BIẾN (CENTER) ---
        initPopularSection();
        
        // --- 4. KHỞI TẠO SỰ KIỆN ---
        initEvents();
    }

    private void initSearchEngine() {
        JPanel pnlSearchWrapper = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    g.setColor(new Color(0, 0, 0, 80)); // Lớp phủ tối mờ
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        pnlSearchWrapper.setPreferredSize(new Dimension(0, 420));

        // Khung tìm kiếm màu trắng bo tròn
        JPanel pnlSearchBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
                g2.dispose();
            }
        };
        pnlSearchBox.setOpaque(false);
        pnlSearchBox.setBackground(new Color(255, 255, 255, 245));
        pnlSearchBox.setLayout(new BoxLayout(pnlSearchBox, BoxLayout.Y_AXIS));
        pnlSearchBox.setPreferredSize(new Dimension(850, 150)); 
        pnlSearchBox.setBorder(new EmptyBorder(25, 40, 25, 40));

        // Hàng 1: Inputs
        JPanel pnlInputs = new JPanel(new GridLayout(1, 4, 25, 0));
        pnlInputs.setOpaque(false);
        
        cbDepGa = new JComboBox<>();
        cbDesGa = new JComboBox<>();
        loadGaData(); // Đổ dữ liệu từ SQL

        dateDep = new JDateChooser();
        dateDep.setDateFormatString("dd/MM/yyyy");
        dateDep.setDate(new Date()); // Mặc định ngày hiện tại

        dateReturn = new JDateChooser();
        dateReturn.setDateFormatString("dd/MM/yyyy");

        pnlInputs.add(createInputGroup("Ga đi", cbDepGa));
        pnlInputs.add(createInputGroup("Ga đến", cbDesGa));
        pnlInputs.add(createInputGroup("Ngày đi", dateDep));
        
        pnlReturnWrapper = createInputGroup("Ngày về", dateReturn);
        pnlReturnWrapper.setVisible(false); // Mặc định ẩn
        pnlInputs.add(pnlReturnWrapper);

        // Hàng 2: Radio & Button
        JPanel pnlBottomSearch = new JPanel(new BorderLayout());
        pnlBottomSearch.setOpaque(false);
        pnlBottomSearch.setBorder(new EmptyBorder(20, 0, 0, 0));

        JPanel pnlRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlRadio.setOpaque(false);
        rdOneWay = new JRadioButton("Một chiều", true);
        rdRoundTrip = new JRadioButton("Khứ hồi");
        rdOneWay.setOpaque(false); rdRoundTrip.setOpaque(false);
        rdOneWay.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rdRoundTrip.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        ButtonGroup group = new ButtonGroup();
        group.add(rdOneWay); group.add(rdRoundTrip);

        pnlRadio.add(rdOneWay);
        pnlRadio.add(Box.createHorizontalStrut(30));
        pnlRadio.add(rdRoundTrip);

        btnSearch = new JButton("TÌM KIẾM CHUYẾN TÀU");
        btnSearch.setBackground(primaryColor);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSearch.setPreferredSize(new Dimension(250, 45));
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearch.setFocusPainted(false);
        btnSearch.setBorder(BorderFactory.createEmptyBorder());

        pnlBottomSearch.add(pnlRadio, BorderLayout.WEST);
        pnlBottomSearch.add(btnSearch, BorderLayout.EAST);

        pnlSearchBox.add(pnlInputs);
        pnlSearchBox.add(pnlBottomSearch);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(150, 0, 0, 0); 
        pnlSearchWrapper.add(pnlSearchBox, gbc);
        add(pnlSearchWrapper, BorderLayout.NORTH);
    }

    private void initPopularSection() {
        JPanel pnlPopularBody = new JPanel(new BorderLayout());
        pnlPopularBody.setOpaque(false);
        pnlPopularBody.setBorder(new EmptyBorder(35, 60, 35, 60));

        JLabel lblTitle = new JLabel("Các chuyến tàu phổ biến");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBorder(new EmptyBorder(0, 0, 25, 0));
        pnlPopularBody.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlGrid = new JPanel(new GridLayout(1, 4, 30, 0));
        pnlGrid.setOpaque(false);
        pnlGrid.add(createTrainCard("Sài Gòn - Đà Nẵng", "Từ 450.000đ", "src/main/resources/images/danang.jpg"));
        pnlGrid.add(createTrainCard("Hà Nội - Hải Phòng", "Từ 125.000đ", "src/main/resources/images/hp.jpg"));
        pnlGrid.add(createTrainCard("Huế - Đà Nẵng", "Từ 110.000đ", "src/main/resources/images/hue.jpg"));
        pnlGrid.add(createTrainCard("Sài Gòn - Nha Trang", "Từ 390.000đ", "src/main/resources/images/nt.jpg"));

        pnlPopularBody.add(pnlGrid, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(pnlPopularBody);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(242, 245, 250));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initEvents() {
        // Xử lý ẩn hiện ngày về
        rdRoundTrip.addItemListener(e -> {
            pnlReturnWrapper.setVisible(e.getStateChange() == ItemEvent.SELECTED);
            revalidate();
            repaint();
        });

        // Xử lý nút tìm kiếm
        btnSearch.addActionListener(e -> {
            String gaDi = cbDepGa.getSelectedItem().toString();
            String gaDen = cbDesGa.getSelectedItem().toString();
            Date ngayDi = dateDep.getDate();

            if (gaDi.equals(gaDen)) {
                JOptionPane.showMessageDialog(this, "Ga đi và ga đến không được trùng nhau!");
                return;
            }
            
            System.out.println("Đang tìm kiếm chuyến từ " + gaDi + " đến " + gaDen + " ngày " + ngayDi);
            // Tại đây bạn sẽ viết logic chuyển Tab hoặc gọi DAO tìm chuyến tàu
        });
    }

    private void loadGaData() {
        DAO_Ga DAO_Ga = new DAO_Ga();
        List<Ga> list = DAO_Ga.getAllGa();
        for (Ga g : list) {
            cbDepGa.addItem(g.getTenGa());
            cbDesGa.addItem(g.getTenGa());
        }
    }

    private JPanel createInputGroup(String labelName, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setOpaque(false);
        JLabel lbl = new JLabel(labelName);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        comp.setPreferredSize(new Dimension(0, 42));
        comp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        p.add(lbl, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private JPanel createTrainCard(String route, String price, String imgPath) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JLabel lblImg = new JLabel();
        lblImg.setPreferredSize(new Dimension(0, 170));
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon(imgPath);
            Image img = icon.getImage().getScaledInstance(280, 170, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblImg.setText("No Image");
            lblImg.setOpaque(true);
            lblImg.setBackground(Color.LIGHT_GRAY);
        }

        JPanel pnlInfo = new JPanel(new GridLayout(2, 1, 5, 5));
        pnlInfo.setBorder(new EmptyBorder(15, 15, 15, 15));
        pnlInfo.setOpaque(false);

        JLabel lblRoute = new JLabel(route);
        lblRoute.setFont(new Font("Segoe UI", Font.BOLD, 15));
        JLabel lblPrice = new JLabel(price);
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblPrice.setForeground(new Color(255, 102, 0));

        pnlInfo.add(lblRoute);
        pnlInfo.add(lblPrice);

        card.add(lblImg, BorderLayout.NORTH);
        card.add(pnlInfo, BorderLayout.CENTER);
        return card;
    }
}