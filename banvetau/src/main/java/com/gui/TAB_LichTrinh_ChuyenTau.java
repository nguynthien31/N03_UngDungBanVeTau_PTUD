package com.gui;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;

/**
 * Tab Quản lý Lịch trình & Chuyến tàu.
 * Chức năng: Thêm, cập nhật, xóa, tìm kiếm chuyến tàu.
 * Trường: Mã chuyến, Tên tàu, Ga đi, Ga đến, Ngày KH, Giờ KH, Giờ cập bến, Trạng thái.
 */
public class TAB_LichTrinh_ChuyenTau extends JPanel {

    // =========================================================================
    // BẢNG MÀU
    // =========================================================================
    private static final Color BG_PAGE     = new Color(0xF4F7FB);
    private static final Color BG_CARD     = Color.WHITE;
    private static final Color ACCENT      = new Color(0x1A5EAB);
    private static final Color ACCENT_HVR  = new Color(0x2270CC);
    private static final Color ACCENT_FOC  = new Color(0x4D9DE0);
    private static final Color TEXT_DARK   = new Color(0x1E2B3C);
    private static final Color TEXT_MID    = new Color(0x5A6A7D);
    private static final Color TEXT_LIGHT  = new Color(0xA0AEC0);
    private static final Color BORDER      = new Color(0xE2EAF4);
    private static final Color ROW_ALT     = new Color(0xF7FAFF);
    private static final Color ROW_SEL     = new Color(0xDDEEFF);
    private static final Color BTN2_BG     = new Color(0xF0F4FA);
    private static final Color BTN2_FG     = new Color(0x3A5A8C);
    private static final Color BTN_RED     = new Color(0xC0392B);
    private static final Color BTN_RED_HVR = new Color(0xE74C3C);

    // =========================================================================
    // FONT
    // =========================================================================
    private static final Font F_TITLE = new Font("Segoe UI", Font.BOLD,  22);
    private static final Font F_LABEL = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font F_CELL  = new Font("Segoe UI", Font.PLAIN, 13);

    // =========================================================================
    // CỘT BẢNG – CỐ ĐỊNH
    // =========================================================================
    private static final String[] COLS = {
            "Mã chuyến", "Tên tàu", "Ga đi", "Ga đến",
            "Ngày KH", "Giờ khởi hành", "Giờ cập bến", "Trạng thái"
    };

    // =========================================================================
    // DANH SÁCH TỈNH/THÀNH PHỐ VIỆT NAM (63 tỉnh thành)
    // =========================================================================
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

    // =========================================================================
    // DANH SÁCH GIỜ 24H (00:00 – 23:30, bước 30 phút)
    // =========================================================================
    private static final String[] GIO_24H = taoGio24H();

    private static String[] taoGio24H() {
        String[] gio = new String[48];
        for (int h = 0; h < 24; h++) {
            gio[h * 2]     = String.format("%02d:00", h);
            gio[h * 2 + 1] = String.format("%02d:30", h);
        }
        return gio;
    }

    // =========================================================================
    // DANH SÁCH TRẠNG THÁI
    // =========================================================================
    private static final String[] TRANG_THAI = {
            "Chờ khởi hành", "Đúng giờ", "Trễ", "Hủy"
    };

    // =========================================================================
    // ĐỊNH DẠNG NGÀY
    // =========================================================================
    private static final String DATE_FORMAT = "dd/MM/yyyy";

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
    private final DatePickerField      dpFilterNgay;  // Bộ lọc theo ngày khởi hành

    // =========================================================================
    // KHỞI TẠO
    // =========================================================================
    public TAB_LichTrinh_ChuyenTau() {
        setLayout(new BorderLayout(0, 16));
        setBackground(BG_PAGE);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // Khởi tạo model và bảng trước
        tableModel = new DefaultTableModel(COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = buildTable();

        // ComboBox tìm kiếm – thêm mục "Tất cả" đầu danh sách
        cbFilterGaDi  = makeComboBox(true);
        cbFilterGaDen = makeComboBox(true);
        // DatePicker lọc theo ngày khởi hành (mặc định hôm nay)
        dpFilterNgay  = makeDatePicker(null);

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
        JLabel lblTitle = new JLabel("QUẢN LÝ LỊCH TRÌNH & CHUYẾN TÀU");
        lblTitle.setFont(F_TITLE);
        lblTitle.setForeground(ACCENT);
        titleRow.add(lblTitle);

        JLabel lblSub = new JLabel("  Thêm, cập nhật và quản lý lịch trình các chuyến tàu");
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
        // Dùng FlowLayout với padding nhỏ gọn, thu nhỏ ComboBox để vừa 1 hàng
        JPanel card = buildCard(new FlowLayout(FlowLayout.LEFT, 14, 14));

        JButton btnTimKiem = makeBtn("Tìm kiếm", BtnStyle.PRIMARY);
        JButton btnLamMoi  = makeBtn("Làm mới",  BtnStyle.SECONDARY);

        btnTimKiem.addActionListener(e -> timKiemChuyenTau());
        btnLamMoi.addActionListener(e -> {
            cbFilterGaDi.setSelectedIndex(0);
            cbFilterGaDen.setSelectedIndex(0);
            // Reset ngày về hôm nay
            dpFilterNgay.resetToToday();
            timKiemChuyenTau();
        });

        // ComboBox tự co theo nội dung như TAB_Gia, DatePicker đủ rộng hiển thị dd/MM/yyyy
        dpFilterNgay.setPreferredSize(new Dimension(160, 36));

        card.add(makeLabel("Ga đi:"));
        card.add(cbFilterGaDi);
        card.add(Box.createHorizontalStrut(4));
        card.add(makeLabel("Ga đến:"));
        card.add(cbFilterGaDen);
        card.add(Box.createHorizontalStrut(4));
        card.add(makeLabel("Ngày KH:"));
        card.add(dpFilterNgay);
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

        JLabel lblSection = new JLabel("Danh sách chuyến tàu");
        lblSection.setFont(F_LABEL);
        lblSection.setForeground(TEXT_DARK);

        JPanel btnGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnGroup.setOpaque(false);

        JButton btnThem    = makeBtn("+ Thêm mới", BtnStyle.PRIMARY);
        JButton btnCapNhat = makeBtn("Cập nhật",   BtnStyle.SECONDARY);
        JButton btnXoa     = makeBtn("Xóa",         BtnStyle.DANGER);

        btnThem.addActionListener(e    -> moDialogThem());
        btnCapNhat.addActionListener(e -> moDialogCapNhat());
        btnXoa.addActionListener(e     -> xoaChuyenTau());

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
        scroll.setPreferredSize(new Dimension(0, 360));
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

        int[] widths = { 100, 110, 120, 120, 100, 110, 110, 110 };
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

    /** Tìm kiếm chuyến tàu theo ga đi / ga đến được chọn */
    private void timKiemChuyenTau() {
        String gaDi  = (String) cbFilterGaDi.getSelectedItem();
        String gaDen = (String) cbFilterGaDen.getSelectedItem();
        String ngayKH = dpFilterNgay.getSelectedDate();

        // TODO: thay bằng truy vấn database thực tế
        JOptionPane.showMessageDialog(this,
                "Tìm kiếm:" +
                        "\n  Ga đi   = \"" + gaDi + "\"" +
                        "\n  Ga đến  = \"" + gaDen + "\"" +
                        "\n  Ngày KH = \"" + ngayKH + "\"" +
                        "\n(Chưa kết nối dữ liệu)",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Mở dialog thêm chuyến tàu mới */
    private void moDialogThem() {
        JDialog dialog = taoDialog("Thêm chuyến tàu mới");

        JTextField        txtMaChuyen  = makeField("VD: CT001");
        JTextField        txtTenTau    = makeField("VD: SE1");
        JComboBox<String> cbGaDi       = makeComboBox(false);
        JComboBox<String> cbGaDen      = makeComboBox(false);
        DatePickerField   spNgayKH     = makeDatePicker(null);
        JComboBox<String> cbGioKH      = makeGioComboBox("06:00");
        JComboBox<String> cbGioCapBen  = makeGioComboBox("12:00");
        JComboBox<String> cbTrangThai  = makeComboBoxFromArray(TRANG_THAI);

        JPanel form = buildForm(
                new String[]{ "Mã chuyến:", "Tên tàu:", "Ga đi:", "Ga đến:",
                        "Ngày khởi hành:", "Giờ khởi hành:", "Giờ cập bến:", "Trạng thái:" },
                new JComponent[]{ txtMaChuyen, txtTenTau, cbGaDi, cbGaDen,
                        spNgayKH, cbGioKH, cbGioCapBen, cbTrangThai }
        );

        JButton btnLuu = makeBtn("Lưu", BtnStyle.PRIMARY);
        btnLuu.addActionListener(e -> {
            if (kiemTraRong(txtMaChuyen, txtTenTau)) {
                tableModel.addRow(new Object[]{
                        txtMaChuyen.getText().trim(),
                        txtTenTau.getText().trim(),
                        cbGaDi.getSelectedItem(),
                        cbGaDen.getSelectedItem(),
                        spNgayKH.getSelectedDate(),
                        cbGioKH.getSelectedItem(),
                        cbGioCapBen.getSelectedItem(),
                        cbTrangThai.getSelectedItem()
                });
                dialog.dispose();
            }
        });

        hienThiDialog(dialog, form, btnLuu);
    }

    /** Mở dialog cập nhật chuyến tàu đang chọn */
    private void moDialogCapNhat() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một chuyến tàu cần cập nhật.",
                    "Chưa chọn dòng", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = taoDialog("Cập nhật chuyến tàu");

        // Lấy giá trị hiện tại từ bảng
        JTextField        txtMaChuyen = makeFieldWithValue(tableModel.getValueAt(row, 0));
        JTextField        txtTenTau   = makeFieldWithValue(tableModel.getValueAt(row, 1));
        JComboBox<String> cbGaDi      = makeComboBox(false);
        JComboBox<String> cbGaDen     = makeComboBox(false);
        DatePickerField   spNgayKH    = makeDatePicker(tableModel.getValueAt(row, 4));
        JComboBox<String> cbGioKH     = makeGioComboBox(strOf(tableModel.getValueAt(row, 5)));
        JComboBox<String> cbGioCapBen = makeGioComboBox(strOf(tableModel.getValueAt(row, 6)));
        JComboBox<String> cbTrangThai = makeComboBoxFromArray(TRANG_THAI);

        // Điền sẵn giá trị ga
        cbGaDi.setSelectedItem(tableModel.getValueAt(row, 2));
        cbGaDen.setSelectedItem(tableModel.getValueAt(row, 3));
        cbTrangThai.setSelectedItem(tableModel.getValueAt(row, 7));

        // Mã chuyến không cho sửa
        txtMaChuyen.setEditable(false);
        txtMaChuyen.setBackground(new Color(0xEEF2F8));

        JPanel form = buildForm(
                new String[]{ "Mã chuyến:", "Tên tàu:", "Ga đi:", "Ga đến:",
                        "Ngày khởi hành:", "Giờ khởi hành:", "Giờ cập bến:", "Trạng thái:" },
                new JComponent[]{ txtMaChuyen, txtTenTau, cbGaDi, cbGaDen,
                        spNgayKH, cbGioKH, cbGioCapBen, cbTrangThai }
        );

        JButton btnLuu = makeBtn("Cập nhật", BtnStyle.PRIMARY);
        btnLuu.addActionListener(e -> {
            if (kiemTraRong(txtTenTau)) {
                tableModel.setValueAt(txtTenTau.getText().trim(),    row, 1);
                tableModel.setValueAt(cbGaDi.getSelectedItem(),      row, 2);
                tableModel.setValueAt(cbGaDen.getSelectedItem(),     row, 3);
                tableModel.setValueAt(
                        spNgayKH.getSelectedDate(), row, 4);
                tableModel.setValueAt(cbGioKH.getSelectedItem(),     row, 5);
                tableModel.setValueAt(cbGioCapBen.getSelectedItem(), row, 6);
                tableModel.setValueAt(cbTrangThai.getSelectedItem(), row, 7);
                dialog.dispose();
            }
        });

        hienThiDialog(dialog, form, btnLuu);
    }

    /** Xóa chuyến tàu đang chọn sau khi xác nhận */
    private void xoaChuyenTau() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một chuyến tàu cần xóa.",
                    "Chưa chọn dòng", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String maChuyen = tableModel.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa chuyến \"" + maChuyen + "\" không?",
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
        dialog.setMinimumSize(new Dimension(440, dialog.getHeight()));
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

    /** Chuyển Object sang String an toàn */
    private String strOf(Object value) {
        return value != null ? value.toString() : "";
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

    /** Nhãn chữ đậm */
    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_LABEL);
        l.setForeground(TEXT_MID);
        return l;
    }

    /** Ô nhập liệu có placeholder mờ */
    private JTextField makeField(String hint) {
        JTextField tf = new JTextField(13) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
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
     * Tạo ComboBox danh sách tỉnh/thành phố.
     * @param coTatCa true → thêm mục "Tất cả" đầu danh sách (dùng cho bộ lọc)
     */
    private JComboBox<String> makeComboBox(boolean coTatCa) {
        String[] items;
        if (coTatCa) {
            items = new String[TINH_THANH.length + 1];
            items[0] = "Tất cả";
            System.arraycopy(TINH_THANH, 0, items, 1, TINH_THANH.length);
        } else {
            items = TINH_THANH;
        }
        JComboBox<String> cb = new JComboBox<>(items);
        styleComboBox(cb);
        return cb;
    }

    /** Tạo ComboBox từ mảng tùy ý (dùng cho trạng thái) */
    private JComboBox<String> makeComboBoxFromArray(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        styleComboBox(cb);
        return cb;
    }

    /**
     * Tạo ComboBox chọn giờ 24h.
     * @param defaultValue giờ mặc định chọn sẵn (VD: "06:00")
     */
    private JComboBox<String> makeGioComboBox(String defaultValue) {
        JComboBox<String> cb = new JComboBox<>(GIO_24H);
        styleComboBox(cb);
        cb.setSelectedItem(defaultValue.isEmpty() ? "06:00" : defaultValue);
        return cb;
    }

    /**
     * Tạo DatePickerField – click vào ô để hiện popup lịch chọn ngày.
     * @param existingValue chuỗi ngày dd/MM/yyyy đã có, hoặc null → ngày hôm nay
     */
    private DatePickerField makeDatePicker(Object existingValue) {
        String initVal = (existingValue != null) ? existingValue.toString() : "";
        return new DatePickerField(initVal);
    }

    /** Style chung cho JComboBox */
    private void styleComboBox(JComboBox<String> cb) {
        cb.setFont(F_CELL);
        cb.setBackground(new Color(0xF8FAFD));
        cb.setForeground(TEXT_DARK);
        cb.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(2, 4, 2, 4)
        ));
    }

    /**
     * Tạo nút theo style.
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

    // =========================================================================
    // INNER CLASS: DATE PICKER FIELD
    // =========================================================================

    /**
     * Ô nhập ngày có popup lịch.
     * - Click vào ô → hiện popup lịch tháng.
     * - Chọn tháng/năm bằng ComboBox, click vào ngày để chọn.
     * - Trả kết quả qua getSelectedDate() dạng dd/MM/yyyy.
     */
    private static class DatePickerField extends JPanel {

        private final JTextField   txtHienThi;  // Ô hiển thị ngày đã chọn
        private final Calendar     calendar;     // Lịch nội tại theo dõi tháng/năm đang xem
        private JPanel             pnlLich;      // Panel lưới ngày
        private JComboBox<String>  cbThang;      // ComboBox chọn tháng
        private JComboBox<Integer> cbNam;        // ComboBox chọn năm
        private JWindow            popup;        // Cửa sổ popup lịch

        private static final String[] TEN_THANG = {
                "Tháng 1","Tháng 2","Tháng 3","Tháng 4","Tháng 5","Tháng 6",
                "Tháng 7","Tháng 8","Tháng 9","Tháng 10","Tháng 11","Tháng 12"
        };
        private static final String[] TEN_THU = {"T2","T3","T4","T5","T6","T7","CN"};

        DatePickerField(String existingValue) {
            setLayout(new BorderLayout());
            setOpaque(false);

            // Khởi tạo calendar với ngày được truyền vào hoặc ngày hôm nay
            calendar = Calendar.getInstance();
            if (!existingValue.isEmpty()) {
                try {
                    calendar.setTime(new SimpleDateFormat(DATE_FORMAT).parse(existingValue));
                } catch (java.text.ParseException ignored) { /* giữ ngày hôm nay */ }
            }

            // Ô hiển thị ngày + icon lịch
            txtHienThi = new JTextField(existingValue.isEmpty()
                    ? new SimpleDateFormat(DATE_FORMAT).format(calendar.getTime())
                    : existingValue);
            txtHienThi.setFont(F_CELL);
            txtHienThi.setForeground(TEXT_DARK);
            txtHienThi.setBackground(new Color(0xF8FAFD));
            txtHienThi.setEditable(false);
            txtHienThi.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            txtHienThi.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDER, 1, true),
                    BorderFactory.createEmptyBorder(6, 10, 6, 36)
            ));

            // Icon lịch vẽ bằng Graphics2D
            JLabel lblIcon = new JLabel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(TEXT_MID);
                    int cx = getWidth()/2, cy = getHeight()/2;
                    g2.drawRoundRect(cx-8, cy-7, 16, 14, 3, 3);
                    g2.drawLine(cx-8, cy-4, cx+8, cy-4);
                    g2.drawLine(cx-4, cy-10, cx-4, cy-5);
                    g2.drawLine(cx+4, cy-10, cx+4, cy-5);
                    g2.fillOval(cx-6, cy-1, 3, 3);
                    g2.fillOval(cx-1, cy-1, 3, 3);
                    g2.fillOval(cx+4, cy-1, 3, 3);
                    g2.fillOval(cx-6, cy+3, 3, 3);
                    g2.fillOval(cx-1, cy+3, 3, 3);
                    g2.dispose();
                }
            };
            lblIcon.setPreferredSize(new Dimension(32, 36));
            lblIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setOpaque(false);
            wrapper.add(txtHienThi, BorderLayout.CENTER);
            wrapper.add(lblIcon,    BorderLayout.EAST);
            add(wrapper, BorderLayout.CENTER);

            // Click vào ô hoặc icon đều mở popup
            txtHienThi.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseClicked(java.awt.event.MouseEvent e) { togglePopup(); }
            });
            lblIcon.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseClicked(java.awt.event.MouseEvent e) { togglePopup(); }
            });
        }

        /** Mở hoặc đóng popup lịch */
        private void togglePopup() {
            if (popup != null && popup.isVisible()) {
                popup.dispose();
                popup = null;
                return;
            }
            hienPopup();
        }

        /** Hiển thị popup lịch bên dưới ô nhập */
        private void hienPopup() {
            Window owner = SwingUtilities.getWindowAncestor(this);
            popup = new JWindow(owner);
            popup.setLayout(new BorderLayout());

            JPanel lichPanel = new JPanel(new BorderLayout(0, 6));
            lichPanel.setBackground(BG_CARD);
            lichPanel.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDER, 1),
                    BorderFactory.createEmptyBorder(12, 12, 12, 12)
            ));

            lichPanel.add(buildNavBar(),   BorderLayout.NORTH);
            pnlLich = new JPanel(new GridLayout(0, 7, 2, 2)); // 0 = số hàng tự động theo nội dung
            pnlLich.setBackground(BG_CARD);
            lichPanel.add(pnlLich,         BorderLayout.CENTER);

            dienLich();
            popup.add(lichPanel);
            popup.pack();
            popup.setMinimumSize(new Dimension(380, popup.getHeight()));
            popup.setSize(Math.max(380, popup.getWidth()), popup.getHeight());

            // Tính vị trí bên dưới ô nhập
            Point loc = txtHienThi.getLocationOnScreen();
            popup.setLocation(loc.x, loc.y + txtHienThi.getHeight() + 2);
            popup.setVisible(true);

            // Đóng popup khi click ra ngoài
            popup.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
                @Override public void windowGainedFocus(java.awt.event.WindowEvent e) {}
                @Override public void windowLostFocus(java.awt.event.WindowEvent e) {
                    if (popup != null) { popup.dispose(); popup = null; }
                }
            });
        }

        /** Thanh điều hướng tháng/năm: nút prev, ComboBox tháng, ComboBox năm, nút next */
        private JPanel buildNavBar() {
            JPanel nav = new JPanel(new BorderLayout(4, 0));
            nav.setBackground(BG_CARD);

            // Nút tháng trước
            JButton btnPrev = makeNavBtn("<");
            btnPrev.addActionListener(e -> {
                calendar.add(Calendar.MONTH, -1);
                cbThang.setSelectedIndex(calendar.get(Calendar.MONTH));
                cbNam.setSelectedItem(calendar.get(Calendar.YEAR));
                dienLich();
            });

            // Nút tháng sau
            JButton btnNext = makeNavBtn(">");
            btnNext.addActionListener(e -> {
                calendar.add(Calendar.MONTH, 1);
                cbThang.setSelectedIndex(calendar.get(Calendar.MONTH));
                cbNam.setSelectedItem(calendar.get(Calendar.YEAR));
                dienLich();
            });

            // ComboBox chọn tháng
            cbThang = new JComboBox<>(TEN_THANG);
            cbThang.setFont(F_LABEL);
            cbThang.setSelectedIndex(calendar.get(Calendar.MONTH));
            cbThang.setPreferredSize(new Dimension(100, 28));
            cbThang.addActionListener(e -> {
                calendar.set(Calendar.MONTH, cbThang.getSelectedIndex());
                dienLich();
            });

            // ComboBox chọn năm (5 năm trước đến 10 năm sau)
            int namHienTai = Calendar.getInstance().get(Calendar.YEAR);
            Integer[] dsNam = new Integer[16];
            for (int i = 0; i < 16; i++) dsNam[i] = namHienTai - 5 + i;
            cbNam = new JComboBox<>(dsNam);
            cbNam.setFont(F_LABEL);
            cbNam.setSelectedItem(calendar.get(Calendar.YEAR));
            cbNam.setPreferredSize(new Dimension(72, 28));
            cbNam.addActionListener(e -> {
                if (cbNam.getSelectedItem() != null) {
                    calendar.set(Calendar.YEAR, (Integer) cbNam.getSelectedItem());
                    dienLich();
                }
            });

            JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
            center.setBackground(BG_CARD);
            center.add(cbThang);
            center.add(cbNam);

            nav.add(btnPrev,  BorderLayout.WEST);
            nav.add(center,   BorderLayout.CENTER);
            nav.add(btnNext,  BorderLayout.EAST);
            return nav;
        }

        /** Vẽ lưới lịch: hàng đầu là tên thứ, các hàng còn lại là số ngày */
        private void dienLich() {
            pnlLich.removeAll();

            // Hàng tiêu đề thứ trong tuần
            for (String thu : TEN_THU) {
                JLabel lbl = new JLabel(thu, SwingConstants.CENTER);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lbl.setPreferredSize(new Dimension(44, 28));
                lbl.setForeground(TEXT_MID);
                pnlLich.add(lbl);
            }

            // Lấy ngày 1 tháng này là thứ mấy (đổi sang Monday-first)
            Calendar temp = (Calendar) calendar.clone();
            temp.set(Calendar.DAY_OF_MONTH, 1);
            int thuDau = (temp.get(Calendar.DAY_OF_WEEK) + 5) % 7; // 0=T2, 6=CN

            // Ngày hôm nay để tô màu
            Calendar homNay = Calendar.getInstance();
            int ngayHomNay = homNay.get(Calendar.DAY_OF_MONTH);
            boolean cungThangNam = homNay.get(Calendar.MONTH)  == calendar.get(Calendar.MONTH)
                    && homNay.get(Calendar.YEAR)   == calendar.get(Calendar.YEAR);

            // Ngày đang được chọn
            int ngayDangChon = -1;
            try {
                Calendar chosen = Calendar.getInstance();
                chosen.setTime(new SimpleDateFormat(DATE_FORMAT).parse(txtHienThi.getText()));
                if (chosen.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                        && chosen.get(Calendar.YEAR)  == calendar.get(Calendar.YEAR))
                    ngayDangChon = chosen.get(Calendar.DAY_OF_MONTH);
            } catch (java.text.ParseException ignored) { /* bỏ qua */ }

            // Ô trống đầu tháng
            for (int i = 0; i < thuDau; i++)
                pnlLich.add(new JLabel());

            // Các ô ngày
            int soNgay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            final int ngayChonFinal = ngayDangChon;

            for (int ngay = 1; ngay <= soNgay; ngay++) {
                final int n = ngay;
                boolean laHomNay  = cungThangNam && ngay == ngayHomNay;
                boolean laChon    = ngay == ngayChonFinal;

                JButton btnNgay = new JButton(String.valueOf(ngay)) {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        if (laChon) {
                            g2.setColor(ACCENT);
                            g2.fillOval(1, 1, getWidth()-2, getHeight()-2);
                        } else if (getModel().isRollover()) {
                            g2.setColor(new Color(0xDDEEFF));
                            g2.fillOval(1, 1, getWidth()-2, getHeight()-2);
                        } else if (laHomNay) {
                            g2.setColor(new Color(0xE8F1FB));
                            g2.fillOval(1, 1, getWidth()-2, getHeight()-2);
                        }
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                btnNgay.setFont(new Font("Segoe UI", laHomNay ? Font.BOLD : Font.PLAIN, 12));
                btnNgay.setForeground(laChon ? Color.WHITE
                        : laHomNay ? ACCENT : TEXT_DARK);
                btnNgay.setPreferredSize(new Dimension(44, 44));
                btnNgay.setContentAreaFilled(false);
                btnNgay.setBorderPainted(false);
                btnNgay.setFocusPainted(false);
                btnNgay.setMargin(new Insets(0, 0, 0, 0));
                btnNgay.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                btnNgay.addActionListener(event -> {
                    // Cập nhật giá trị và đóng popup
                    calendar.set(Calendar.DAY_OF_MONTH, n);
                    txtHienThi.setText(new SimpleDateFormat(DATE_FORMAT).format(calendar.getTime()));
                    if (popup != null) { popup.dispose(); popup = null; }
                });

                pnlLich.add(btnNgay);
            }

            pnlLich.revalidate();
            pnlLich.repaint();
        }

        /** Nút điều hướng prev/next nhỏ gọn */
        private JButton makeNavBtn(String text) {
            JButton b = new JButton(text);
            b.setFont(new Font("Segoe UI", Font.BOLD, 14));
            b.setForeground(ACCENT);
            b.setContentAreaFilled(false);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setMargin(new Insets(0, 0, 0, 0));
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.setPreferredSize(new Dimension(32, 32));
            return b;
        }

        /** Trả về ngày đang chọn dạng dd/MM/yyyy */
        public String getSelectedDate() {
            return txtHienThi.getText();
        }

        /** Đặt lại về ngày hôm nay */
        public void resetToToday() {
            calendar.setTime(new Date());
            txtHienThi.setText(new SimpleDateFormat(DATE_FORMAT).format(calendar.getTime()));
        }
    }

    /** Renderer header bảng: nền xanh, chữ trắng */
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
        private static final int S = 4;

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            for (int i = S; i > 0; i--) {
                g2.setColor(new Color(100, 140, 200, (int)(20.0 * (S - i) / S)));
                g2.drawRoundRect(x + i, y + i, w - 2*i - 1, h - 2*i - 1, 12, 12);
            }
            g2.setColor(new Color(0xE2EAF4));
            g2.drawRoundRect(x, y, w - 1, h - 1, 12, 12);
            g2.setColor(BG_CARD);
            g2.setClip(new RoundRectangle2D.Float(x+1, y+1, w-2, h-2, 12, 12));
            g2.fillRect(x+1, y+1, w-2, h-2);
            g2.dispose();
        }

        @Override public Insets getBorderInsets(Component c)             { return new Insets(S,S,S,S); }
        @Override public Insets getBorderInsets(Component c, Insets ins) { ins.set(S,S,S,S); return ins; }
    }
}