package com.gui;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;

/**
 * Tab Quản lý Giá vé & Hạng vé tàu.
 * Chức năng: Thêm, cập nhật, xóa, tìm kiếm giá vé theo tuyến và loại ghế.
 * Hỗ trợ điều chỉnh giá theo mùa cao điểm.
 */
public class TAB_Gia extends JPanel {

    // =========================================================================
    // BẢNG MÀU
    // =========================================================================
    private static final Color BG_PAGE     = new Color(0xF4F7FB);
    private static final Color BG_CARD     = Color.WHITE;
    private static final Color ACCENT      = new Color(0x1A5EAB); // Xanh chủ đạo
    private static final Color ACCENT_HVR  = new Color(0x2270CC); // Hover nút chính
    private static final Color ACCENT_FOC  = new Color(0x4D9DE0); // Viền focus
    private static final Color TEXT_DARK   = new Color(0x1E2B3C);
    private static final Color TEXT_MID    = new Color(0x5A6A7D);
    private static final Color TEXT_LIGHT  = new Color(0xA0AEC0);
    private static final Color BORDER      = new Color(0xE2EAF4);
    private static final Color ROW_ALT     = new Color(0xF7FAFF);
    private static final Color ROW_SEL     = new Color(0xDDEEFF);
    private static final Color BTN2_BG     = new Color(0xF0F4FA);
    private static final Color BTN2_FG     = new Color(0x3A5A8C);
    private static final Color BTN_RED     = new Color(0xC0392B); // Nút xóa
    private static final Color BTN_RED_HVR = new Color(0xE74C3C);

    // =========================================================================
    // FONT
    // =========================================================================
    private static final Font F_TITLE = new Font("Segoe UI", Font.BOLD,  22);
    private static final Font F_LABEL = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font F_CELL  = new Font("Segoe UI", Font.PLAIN, 13);

    // =========================================================================
    // CỘT BẢNG – CỐ ĐỊNH, KHÔNG THAY ĐỔI
    // =========================================================================
    private static final String[] COLS = {
            "Mã giá vé", "Ga đi", "Ga đến", "Loại ghế", "Giá vé (VNĐ)", "Mùa cao điểm"
    };

    // =========================================================================
    // DỮ LIỆU DANH MỤC
    // =========================================================================

    /** 63 tỉnh/thành phố Việt Nam */
    private static final String[] TINH_THANH = {
            "An Giang", "Bà Rịa - Vũng Tàu", "Bắc Giang", "Bắc Kạn", "Bạc Liêu",
            "Bắc Ninh", "Bến Tre", "Bình Định", "Bình Dương", "Bình Phước",
            "Bình Thuận", "Cà Mau", "Cần Thơ", "Cao Bằng", "Đà Nẵng",
            "Đắk Lắk", "Đắk Nông", "Điện Biên", "Đồng Nai", "Đồng Tháp",
            "Gia Lai", "Hà Giang", "Hà Nam", "Hà Nội", "Hà Tĩnh",
            "Hải Dương", "Hải Phòng", "Hậu Giang", "Hòa Bình", "Hưng Yên",
            "Khánh Hòa", "Kiên Giang", "Kon Tum", "Lai Châu", "Lâm Đồng",
            "Lạng Sơn", "Lào Cai", "Long An", "Nam Định", "Nghệ An",
            "Ninh Bình", "Ninh Thuận", "Phú Thọ", "Phú Yên", "Quảng Bình",
            "Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị", "Sóc Trăng",
            "Sơn La", "Tây Ninh", "Thái Bình", "Thái Nguyên", "Thanh Hóa",
            "Thừa Thiên Huế", "Tiền Giang", "TP. Hồ Chí Minh", "Trà Vinh",
            "Tuyên Quang", "Vĩnh Long", "Vĩnh Phúc", "Yên Bái"
    };

    /** Các loại ghế/hạng vé */
    private static final String[] LOAI_GHE = {
            "Ghế ngồi mềm", "Ghế ngồi cứng", "Nằm mềm điều hòa",
            "Nằm mềm không điều hòa", "Nằm cứng điều hòa", "Nằm cứng không điều hòa"
    };

    /** Trạng thái mùa cao điểm */
    private static final String[] MUA_CAO_DIEM = { "Không", "Có" };

    // =========================================================================
    // STYLE NÚT
    // =========================================================================
    private enum BtnStyle { PRIMARY, SECONDARY, DANGER }

    // =========================================================================
    // THÀNH PHẦN DÙNG NHIỀU NƠI
    // =========================================================================
    private final DefaultTableModel   tableModel;
    private final JTable              table;
    private final JComboBox<String>   cbFilterGaDi;
    private final JComboBox<String>   cbFilterGaDen;
    private final JComboBox<String>   cbFilterLoaiGhe;

    // =========================================================================
    // KHỞI TẠO
    // =========================================================================
    public TAB_Gia() {
        setLayout(new BorderLayout(0, 16));
        setBackground(BG_PAGE);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // Khởi tạo model và bảng trước
        tableModel = new DefaultTableModel(COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = buildTable();

        // ComboBox lọc có thêm "Tất cả"
        cbFilterGaDi    = makeComboBox(TINH_THANH, true);
        cbFilterGaDen   = makeComboBox(TINH_THANH, true);
        cbFilterLoaiGhe = makeComboBox(LOAI_GHE,   true);

        add(buildHeader(),     BorderLayout.NORTH);
        add(buildFilterCard(), BorderLayout.CENTER);
        add(buildMainCard(),   BorderLayout.SOUTH);
    }

    // =========================================================================
    // TIÊU ĐỀ
    // =========================================================================
    private JPanel buildHeader() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);
        pnl.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titleRow.setOpaque(false);
        JLabel lblTitle = new JLabel("QUẢN LÝ GIÁ VÉ & HẠNG VÉ");
        lblTitle.setFont(F_TITLE);
        lblTitle.setForeground(ACCENT);
        titleRow.add(lblTitle);

        JLabel lblSub = new JLabel("  Thiết lập và điều chỉnh giá vé theo tuyến, loại ghế và mùa cao điểm");
        lblSub.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblSub.setForeground(TEXT_LIGHT);
        lblSub.setBorder(BorderFactory.createEmptyBorder(2, 6, 0, 0));

        left.add(titleRow);
        left.add(lblSub);
        pnl.add(left, BorderLayout.WEST);
        return pnl;
    }

    // =========================================================================
    // CARD TÌM KIẾM
    // =========================================================================
    private JPanel buildFilterCard() {
        JPanel card = buildCard(new FlowLayout(FlowLayout.LEFT, 14, 14));

        JButton btnTimKiem = makeBtn("Tìm kiếm", BtnStyle.PRIMARY);
        JButton btnLamMoi  = makeBtn("Làm mới",  BtnStyle.SECONDARY);

        btnTimKiem.addActionListener(e -> timKiemGiaVe());
        btnLamMoi.addActionListener(e -> {
            cbFilterGaDi.setSelectedIndex(0);
            cbFilterGaDen.setSelectedIndex(0);
            cbFilterLoaiGhe.setSelectedIndex(0);
            timKiemGiaVe();
        });

        card.add(makeLabel("Ga đi:"));
        card.add(cbFilterGaDi);
        card.add(Box.createHorizontalStrut(4));
        card.add(makeLabel("Ga đến:"));
        card.add(cbFilterGaDen);
        card.add(Box.createHorizontalStrut(4));
        card.add(makeLabel("Loại ghế:"));
        card.add(cbFilterLoaiGhe);
        card.add(Box.createHorizontalStrut(8));
        card.add(btnTimKiem);
        card.add(btnLamMoi);
        return card;
    }

    // =========================================================================
    // CARD CHÍNH: THANH NÚT + BẢNG
    // =========================================================================
    private JPanel buildMainCard() {
        JPanel card = buildCard(new BorderLayout(0, 0));
        card.add(buildActionBar(),  BorderLayout.NORTH);
        card.add(buildTableBody(), BorderLayout.CENTER);
        return card;
    }

    /** Thanh hành động: nhãn + nút Thêm, Cập nhật, Xóa */
    private JPanel buildActionBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(12, 18, 10, 18));

        JLabel lblSection = new JLabel("Danh sách giá vé");
        lblSection.setFont(F_LABEL);
        lblSection.setForeground(TEXT_DARK);

        JPanel btnGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnGroup.setOpaque(false);

        JButton btnThem    = makeBtn("+ Thêm mới", BtnStyle.PRIMARY);
        JButton btnCapNhat = makeBtn("Cập nhật",   BtnStyle.SECONDARY);
        JButton btnXoa     = makeBtn("Xóa",         BtnStyle.DANGER);

        btnThem.addActionListener(e    -> moDialogThem());
        btnCapNhat.addActionListener(e -> moDialogCapNhat());
        btnXoa.addActionListener(e     -> xoaGiaVe());

        btnGroup.add(btnThem);
        btnGroup.add(btnCapNhat);
        btnGroup.add(btnXoa);

        bar.add(lblSection, BorderLayout.WEST);
        bar.add(btnGroup,   BorderLayout.EAST);
        return bar;
    }

    /** Phần thân bảng: separator + scroll + empty state */
    private JPanel buildTableBody() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BG_CARD);
        scroll.setPreferredSize(new Dimension(0, 380));
        styleScrollBar(scroll.getVerticalScrollBar());
        styleScrollBar(scroll.getHorizontalScrollBar());

        JLabel lblEmpty = new JLabel(
                "Chưa có dữ liệu – nhấn \"+ Thêm mới\" hoặc tìm kiếm",
                SwingConstants.CENTER);
        lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblEmpty.setForeground(TEXT_LIGHT);
        lblEmpty.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        body.add(sep,      BorderLayout.NORTH);
        body.add(scroll,   BorderLayout.CENTER);
        body.add(lblEmpty, BorderLayout.SOUTH);
        return body;
    }

    // =========================================================================
    // XÂY DỰNG BẢNG
    // =========================================================================
    private JTable buildTable() {
        JTable t = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }

            // Tô màu xen kẽ hàng chẵn/lẻ
            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row))
                    c.setBackground(row % 2 == 0 ? BG_CARD : ROW_ALT);
                return c;
            }
        };

        t.setFont(F_CELL);
        t.setRowHeight(38);
        t.setShowVerticalLines(false);
        t.setShowHorizontalLines(true);
        t.setGridColor(BORDER);
        t.setSelectionBackground(ROW_SEL);
        t.setSelectionForeground(TEXT_DARK);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setFocusable(false);
        t.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = t.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer());
        header.setPreferredSize(new Dimension(0, 42));
        header.setReorderingAllowed(false);

        // Độ rộng từng cột
        int[] widths = { 110, 140, 140, 180, 140, 130 };
        for (int i = 0; i < widths.length; i++)
            t.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        applyRenderers(t);
        return t;
    }

    /** Gán renderer padding trái đồng nhất cho tất cả cột */
    private void applyRenderers(JTable t) {
        DefaultTableCellRenderer cellR = new DefaultTableCellRenderer();
        cellR.setFont(F_CELL);
        cellR.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 6));
        for (int i = 0; i < COLS.length; i++)
            t.getColumnModel().getColumn(i).setCellRenderer(cellR);
    }

    // =========================================================================
    // XỬ LÝ NGHIỆP VỤ
    // =========================================================================

    /** Tìm kiếm giá vé theo ga đi, ga đến và loại ghế */
    private void timKiemGiaVe() {
        String gaDi    = (String) cbFilterGaDi.getSelectedItem();
        String gaDen   = (String) cbFilterGaDen.getSelectedItem();
        String loaiGhe = (String) cbFilterLoaiGhe.getSelectedItem();

        // TODO: thay bằng truy vấn database thực tế
        JOptionPane.showMessageDialog(this,
                "Tìm kiếm: Ga đi = \"" + gaDi + "\", Ga đến = \"" + gaDen +
                        "\", Loại ghế = \"" + loaiGhe + "\"\n(Chưa kết nối dữ liệu)",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Mở dialog thêm giá vé mới */
    private void moDialogThem() {
        JDialog dialog = taoDialog("Thêm giá vé mới");

        JTextField        txtMaGiaVe   = makeField("VD: GV001");
        JComboBox<String> cbGaDi       = makeComboBox(TINH_THANH, false);
        JComboBox<String> cbGaDen      = makeComboBox(TINH_THANH, false);
        JComboBox<String> cbLoaiGhe    = makeComboBox(LOAI_GHE,   false);
        JTextField        txtGiaVe     = makeField("VD: 500000");
        JComboBox<String> cbCaoDiem    = makeComboBox(MUA_CAO_DIEM, false);

        JPanel form = buildForm(
                new String[]{ "Mã giá vé:", "Ga đi:", "Ga đến:",
                        "Loại ghế:", "Giá vé (VNĐ):", "Mùa cao điểm:" },
                new JComponent[]{ txtMaGiaVe, cbGaDi, cbGaDen,
                        cbLoaiGhe, txtGiaVe, cbCaoDiem }
        );

        JButton btnLuu = makeBtn("Lưu", BtnStyle.PRIMARY);
        btnLuu.addActionListener(_ -> {
            if (kiemTraRong(txtMaGiaVe, txtGiaVe) && kiemTraSo(txtGiaVe)) {
                tableModel.addRow(new Object[]{
                        txtMaGiaVe.getText().trim(),
                        cbGaDi.getSelectedItem(),
                        cbGaDen.getSelectedItem(),
                        cbLoaiGhe.getSelectedItem(),
                        formatGia(txtGiaVe.getText().trim()),
                        cbCaoDiem.getSelectedItem()
                });
                dialog.dispose();
            }
        });

        hienThiDialog(dialog, form, btnLuu);
    }

    /** Mở dialog cập nhật giá vé đang chọn */
    private void moDialogCapNhat() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một giá vé cần cập nhật.",
                    "Chưa chọn dòng", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = taoDialog("Cập nhật giá vé");

        JTextField        txtMaGiaVe = makeFieldWithValue(tableModel.getValueAt(row, 0));
        JComboBox<String> cbGaDi     = makeComboBox(TINH_THANH, false);
        JComboBox<String> cbGaDen    = makeComboBox(TINH_THANH, false);
        JComboBox<String> cbLoaiGhe  = makeComboBox(LOAI_GHE,   false);
        JTextField        txtGiaVe   = makeFieldWithValue(
                tableModel.getValueAt(row, 4).toString().replaceAll("[^0-9]", ""));
        JComboBox<String> cbCaoDiem  = makeComboBox(MUA_CAO_DIEM, false);

        // Điền sẵn giá trị hiện tại
        cbGaDi.setSelectedItem(tableModel.getValueAt(row, 1));
        cbGaDen.setSelectedItem(tableModel.getValueAt(row, 2));
        cbLoaiGhe.setSelectedItem(tableModel.getValueAt(row, 3));
        cbCaoDiem.setSelectedItem(tableModel.getValueAt(row, 5));

        // Mã giá vé không cho sửa
        txtMaGiaVe.setEditable(false);
        txtMaGiaVe.setBackground(new Color(0xEEF2F8));

        JPanel form = buildForm(
                new String[]{ "Mã giá vé:", "Ga đi:", "Ga đến:",
                        "Loại ghế:", "Giá vé (VNĐ):", "Mùa cao điểm:" },
                new JComponent[]{ txtMaGiaVe, cbGaDi, cbGaDen,
                        cbLoaiGhe, txtGiaVe, cbCaoDiem }
        );

        JButton btnLuu = makeBtn("Cập nhật", BtnStyle.PRIMARY);
        btnLuu.addActionListener(e -> {
            if (kiemTraRong(txtGiaVe) && kiemTraSo(txtGiaVe)) {
                tableModel.setValueAt(cbGaDi.getSelectedItem(),    row, 1);
                tableModel.setValueAt(cbGaDen.getSelectedItem(),   row, 2);
                tableModel.setValueAt(cbLoaiGhe.getSelectedItem(), row, 3);
                tableModel.setValueAt(formatGia(txtGiaVe.getText().trim()), row, 4);
                tableModel.setValueAt(cbCaoDiem.getSelectedItem(), row, 5);
                dialog.dispose();
            }
        });

        hienThiDialog(dialog, form, btnLuu);
    }

    /** Xóa giá vé đang chọn sau khi xác nhận */
    private void xoaGiaVe() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một giá vé cần xóa.",
                    "Chưa chọn dòng", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String ma = tableModel.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa giá vé \"" + ma + "\" không?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION)
            tableModel.removeRow(row);
    }

    // =========================================================================
    // HÀM HỖ TRỢ DIALOG
    // =========================================================================

    /** Tạo dialog modal căn giữa */
    private JDialog taoDialog(String title) {
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        d.setLayout(new BorderLayout());
        d.getContentPane().setBackground(BG_PAGE);
        return d;
    }

    /** Xây dựng form lưới nhãn + component */
    private JPanel buildForm(String[] labels, JComponent[] fields) {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(20, 24, 8, 24));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(7, 6, 7, 6);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill   = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < labels.length; i++) {
            gc.gridx = 0; gc.gridy = i; gc.weightx = 0;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(F_LABEL);
            lbl.setForeground(TEXT_MID);
            form.add(lbl, gc);

            gc.gridx = 1; gc.weightx = 1;
            fields[i].setPreferredSize(new Dimension(260, 36));
            form.add(fields[i], gc);
        }
        return form;
    }

    /** Hiển thị dialog với form và nút lưu/hủy */
    private void hienThiDialog(JDialog dialog, JPanel form, JButton btnLuu) {
        JButton btnHuy = makeBtn("Hủy", BtnStyle.SECONDARY);
        btnHuy.addActionListener(e -> dialog.dispose());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 14));
        btnRow.setOpaque(false);
        btnRow.setBorder(BorderFactory.createEmptyBorder(0, 16, 4, 16));
        btnRow.add(btnHuy);
        btnRow.add(btnLuu);

        dialog.add(form,   BorderLayout.CENTER);
        dialog.add(btnRow, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(420, dialog.getHeight()));
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /** Kiểm tra các trường bắt buộc không được để trống */
    private boolean kiemTraRong(JTextField... fields) {
        for (JTextField f : fields) {
            if (f.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng điền đầy đủ thông tin.",
                        "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                f.requestFocus();
                return false;
            }
        }
        return true;
    }

    /** Kiểm tra trường giá vé phải là số nguyên dương */
    private boolean kiemTraSo(JTextField field) {
        try {
            long gia = Long.parseLong(field.getText().trim().replaceAll("[^0-9]", ""));
            if (gia <= 0) throw new NumberFormatException();
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Giá vé phải là số nguyên dương (VD: 500000).",
                    "Giá trị không hợp lệ", JOptionPane.WARNING_MESSAGE);
            field.requestFocus();
            return false;
        }
    }

    /**
     * Định dạng số giá vé có dấu phân cách hàng nghìn.
     * VD: "500000" → "500.000"
     */
    private String formatGia(String rawGia) {
        try {
            long gia = Long.parseLong(rawGia.replaceAll("[^0-9]", ""));
            return String.format("%,d", gia).replace(',', '.');
        } catch (NumberFormatException e) {
            return rawGia;
        }
    }

    // =========================================================================
    // HÀM TIỆN ÍCH TẠO COMPONENT
    // =========================================================================

    /** Tạo card nền trắng có shadow bo góc */
    private JPanel buildCard(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setBackground(BG_CARD);
        p.setBorder(new ShadowBorder());
        return p;
    }

    /** Nhãn chữ đậm dùng trong form */
    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_LABEL);
        l.setForeground(TEXT_MID);
        return l;
    }

    /** Ô nhập liệu có placeholder mờ, viền đổi màu khi focus */
    private JTextField makeField(String hint) {
        JTextField tf = new JTextField(13) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Vẽ placeholder khi ô rỗng và không focus
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(TEXT_LIGHT);
                    g2.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                    Insets ins = getInsets();
                    g2.drawString(hint, ins.left + 4, getHeight() / 2 + 5);
                    g2.dispose();
                }
            }
        };
        applyFieldStyle(tf);
        return tf;
    }

    /** Ô nhập liệu với giá trị điền sẵn */
    private JTextField makeFieldWithValue(Object value) {
        JTextField tf = new JTextField(value != null ? value.toString() : "");
        applyFieldStyle(tf);
        return tf;
    }

    /** Style chung cho JTextField */
    private void applyFieldStyle(JTextField tf) {
        tf.setFont(F_CELL);
        tf.setForeground(TEXT_DARK);
        tf.setBackground(new Color(0xF8FAFD));
        tf.setPreferredSize(new Dimension(160, 36));
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        // Đổi viền khi focus / mất focus
        tf.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                tf.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(ACCENT_FOC, 2, true),
                        BorderFactory.createEmptyBorder(5, 9, 5, 9)));
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                tf.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER, 1, true),
                        BorderFactory.createEmptyBorder(6, 10, 6, 10)));
            }
        });
    }

    /**
     * Tạo JComboBox từ mảng dữ liệu.
     * @param items    mảng dữ liệu hiển thị
     * @param coTatCa  true → thêm mục "Tất cả" đầu danh sách (dùng cho bộ lọc)
     */
    private JComboBox<String> makeComboBox(String[] items, boolean coTatCa) {
        String[] data;
        if (coTatCa) {
            data = new String[items.length + 1];
            data[0] = "Tất cả";
            System.arraycopy(items, 0, data, 1, items.length);
        } else {
            data = items;
        }
        JComboBox<String> cb = new JComboBox<>(data);
        cb.setFont(F_CELL);
        cb.setBackground(new Color(0xF8FAFD));
        cb.setForeground(TEXT_DARK);
        cb.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(2, 4, 2, 4)
        ));
        return cb;
    }

    /**
     * Tạo nút theo style chỉ định.
     * PRIMARY → xanh | SECONDARY → xám | DANGER → đỏ
     */
    private JButton makeBtn(String text, BtnStyle style) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                // SỬA LẠI THÀNH:
                switch (style) {
                    case PRIMARY:
                        g2.setColor(getModel().isPressed() ? new Color(0x0F3F8C)
                                : getModel().isRollover() ? ACCENT_HVR : ACCENT);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                        break;
                    case DANGER:
                        g2.setColor(getModel().isPressed() ? new Color(0x922B21)
                                : getModel().isRollover() ? BTN_RED_HVR : BTN_RED);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                        break;
                    default:
                        // code cho default
                        break;
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(F_LABEL);
        btn.setForeground(style == BtnStyle.SECONDARY ? BTN2_FG : Color.WHITE);
        // Cách cũ cho Java 8:
        int width = 110;
        if (style == BtnStyle.PRIMARY) width = 130;
        else if (style == BtnStyle.DANGER) width = 80;

        btn.setPreferredSize(new Dimension(width, 36));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /** Tùy chỉnh scrollbar: mỏng, không mũi tên */
    private void styleScrollBar(JScrollBar sb) {
        sb.setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = new Color(0xC0D4EE);
                trackColor = BG_PAGE;
            }
            @Override protected JButton createDecreaseButton(int o) { return zeroBtn(); }
            @Override protected JButton createIncreaseButton(int o) { return zeroBtn(); }
            private JButton zeroBtn() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });
    }

    // =========================================================================
    // INNER CLASSES
    // =========================================================================

    /** Renderer header bảng: nền xanh đậm, chữ trắng */
    private static class HeaderRenderer extends DefaultTableCellRenderer {
        HeaderRenderer() { setHorizontalAlignment(LEFT); }

        @Override
        public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int row, int col) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, row, col);
            l.setOpaque(true);
            l.setBackground(ACCENT);
            l.setForeground(Color.WHITE);
            l.setFont(new Font("Segoe UI", Font.BOLD, 13));
            l.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 6));
            return l;
        }
    }

    /** Border shadow nhẹ bo góc cho card */
    private static class ShadowBorder extends AbstractBorder {
        private static final int S = 4; // Kích thước vùng bóng đổ (px)

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            // Vẽ các lớp bóng với alpha giảm dần
            for (int i = S; i > 0; i--) {
                g2.setColor(new Color(100, 140, 200, (int)(20.0 * (S - i) / S)));
                g2.drawRoundRect(x + i, y + i, w - 2*i - 1, h - 2*i - 1, 12, 12);
            }
            // Viền ngoài card
            g2.setColor(new Color(0xE2EAF4));
            g2.drawRoundRect(x, y, w - 1, h - 1, 12, 12);
            // Tô trắng nền bên trong (clip bo góc)
            g2.setColor(BG_CARD);
            g2.setClip(new RoundRectangle2D.Float(x+1, y+1, w-2, h-2, 12, 12));
            g2.fillRect(x+1, y+1, w-2, h-2);
            g2.dispose();
        }

        @Override public Insets getBorderInsets(Component c)             { return new Insets(S,S,S,S); }
        @Override public Insets getBorderInsets(Component c, Insets ins) { ins.set(S,S,S,S); return ins; }
    }
}