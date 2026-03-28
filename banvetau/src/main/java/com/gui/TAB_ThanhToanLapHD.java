package com.gui;

import com.connectDB.ConnectDB;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class TAB_ThanhToanLapHD extends JPanel {
    private JTextField txtMaHDMoi, txtTenKHMoi, txtTongTienMoi, txtKhuyenMaiMoi;
    private DefaultTableModel modelVeChoThanhToan, modelLichSu;
    private JTable tableVeChoThanhToan, tableLichSu;
    private JButton btnXacNhan;
    private JComboBox<Object> cbNhanVienLoc;
    private JDateChooser dateLoc;
    private JTextField txtTimKiemMaHD;

    private DecimalFormat df = new DecimalFormat("#,### VNĐ");
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public TAB_ThanhToanLapHD(String maHDInitial) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setDividerLocation(550);
        mainSplit.setDividerSize(8);

        mainSplit.setLeftComponent(createPanelThanhToan());
        mainSplit.setRightComponent(createPanelLichSu());

        add(mainSplit, BorderLayout.CENTER);
        
        loadNhanVienToCombo();
        locLichSu();

        if (maHDInitial != null && !maHDInitial.isEmpty()) {
            loadDuLieuThanhToanMoi(maHDInitial);
        }
    }

    private JPanel createPanelThanhToan() {
        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBorder(new TitledBorder(new LineBorder(Color.BLUE), "XỬ LÝ THANH TOÁN MỚI"));
        pnl.setBackground(Color.WHITE);

        JPanel pnlInfo = new JPanel(new GridLayout(4, 2, 10, 10));
        pnlInfo.setBackground(Color.WHITE);
        pnlInfo.setBorder(new EmptyBorder(10, 10, 10, 10));

        pnlInfo.add(new JLabel("Mã Hóa Đơn:"));
        txtMaHDMoi = new JTextField(); txtMaHDMoi.setEditable(false);
        pnlInfo.add(txtMaHDMoi);

        pnlInfo.add(new JLabel("Khách hàng:"));
        txtTenKHMoi = new JTextField(); txtTenKHMoi.setEditable(false);
        pnlInfo.add(txtTenKHMoi);

        pnlInfo.add(new JLabel("Khuyến mãi:"));
        txtKhuyenMaiMoi = new JTextField(); txtKhuyenMaiMoi.setEditable(false);
        pnlInfo.add(txtKhuyenMaiMoi);

        modelVeChoThanhToan = new DefaultTableModel(new String[]{"Mã Vé", "Loại Vé", "Giá Tiền"}, 0);
        tableVeChoThanhToan = new JTable(modelVeChoThanhToan);
        
        JPanel pnlSouth = new JPanel(new BorderLayout());
        txtTongTienMoi = new JTextField("0 VNĐ");
        txtTongTienMoi.setFont(new Font("Arial", Font.BOLD, 22));
        txtTongTienMoi.setForeground(Color.RED);
        txtTongTienMoi.setHorizontalAlignment(JTextField.RIGHT);
        txtTongTienMoi.setEditable(false);
        
        btnXacNhan = new JButton("XÁC NHẬN & IN HÓA ĐƠN");
        btnXacNhan.setBackground(new Color(0, 120, 215));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setPreferredSize(new Dimension(0, 50));
        btnXacNhan.setEnabled(false);
        btnXacNhan.addActionListener(e -> thanhToanThanhCong());

        pnlSouth.add(txtTongTienMoi, BorderLayout.NORTH);
        pnlSouth.add(btnXacNhan, BorderLayout.SOUTH);

        pnl.add(pnlInfo, BorderLayout.NORTH);
        pnl.add(new JScrollPane(tableVeChoThanhToan), BorderLayout.CENTER);
        pnl.add(pnlSouth, BorderLayout.SOUTH);

        return pnl;
    }

    private JPanel createPanelLichSu() {
        JPanel pnl = new JPanel(new BorderLayout(5, 5));
        pnl.setBorder(new TitledBorder("LỊCH SỬ GIAO DỊCH"));

        // Bộ lọc
        JPanel pnlFilter = new JPanel(new GridLayout(3, 2, 5, 5));
        pnlFilter.add(new JLabel(" Tìm Mã HD:"));
        txtTimKiemMaHD = new JTextField();
        txtTimKiemMaHD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) { locLichSu(); }
        });
        pnlFilter.add(txtTimKiemMaHD);

        pnlFilter.add(new JLabel(" Nhân viên:"));
        cbNhanVienLoc = new JComboBox<>();
        cbNhanVienLoc.addActionListener(e -> locLichSu());
        pnlFilter.add(cbNhanVienLoc);

        pnlFilter.add(new JLabel(" Ngày lập:"));
        dateLoc = new JDateChooser();
        dateLoc.addPropertyChangeListener("date", e -> locLichSu());
        pnlFilter.add(dateLoc);

        // Bảng lịch sử - KHÓA CHỈNH SỬA
        modelLichSu = new DefaultTableModel(new String[]{"Mã HD", "Ngày lập", "Tổng tiền"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa bất kỳ ô nào
            }
        };
        tableLichSu = new JTable(modelLichSu);
        tableLichSu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableLichSu.getTableHeader().setReorderingAllowed(false); // Không cho kéo đổi cột

        // SỰ KIỆN DOUBLE CLICK
        tableLichSu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tableLichSu.getSelectedRow();
                    if (row != -1) {
                        String maHD = tableLichSu.getValueAt(row, 0).toString();
                        hienThiChiTietInLai(maHD);
                    }
                }
            }
        });

        pnl.add(pnlFilter, BorderLayout.NORTH);
        pnl.add(new JScrollPane(tableLichSu), BorderLayout.CENTER);
        pnl.add(new JLabel(" (*) Nhấp đúp để xem chi tiết và in lại", JLabel.CENTER), BorderLayout.SOUTH);

        return pnl;
    }

    private void hienThiChiTietInLai(String maHD) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết hóa đơn", true);
        dlg.setSize(450, 600);
        dlg.setLocationRelativeTo(this);

        JTextArea area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setEditable(false);
        area.setMargin(new Insets(10, 10, 10, 10));

        StringBuilder sb = new StringBuilder();
        sb.append("        HÓA ĐƠN\n");
        sb.append("           GA ĐƯỜNG SẮT Việt Nam\n");
        sb.append("==========================================\n");

        try (Connection con = ConnectDB.getConnection()) {
            // 1. Lấy thông tin Master
            String sql = "SELECT h.*, n.tenNV, k.tenKH FROM HoaDon h " +
                         "JOIN NhanVien n ON h.maNV = n.maNV " +
                         "JOIN KhachHang k ON h.maKH = k.maKH WHERE h.maHD = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                sb.append("Mã hóa đơn: ").append(rs.getString("maHD")).append("\n");
                sb.append("Ngày lập:   ").append(sdf.format(rs.getTimestamp("ngayLap"))).append("\n");
                sb.append("Nhân viên:  ").append(rs.getString("tenNV")).append("\n");
                sb.append("Khách hàng: ").append(rs.getString("tenKH")).append("\n");
                sb.append("------------------------------------------\n");
                sb.append(String.format("%-15s %-10s %12s\n", "Mã Vé", "Loại", "Giá"));

                // 2. Lấy danh sách vé
                String sqlCT = "SELECT ct.maVe, v.maLoaiVe, ct.donGia FROM ChiTietHoaDon ct " +
                               "JOIN Ve v ON ct.maVe = v.maVe WHERE ct.maHD = ?";
                PreparedStatement ps2 = con.prepareStatement(sqlCT);
                ps2.setString(1, maHD);
                ResultSet rs2 = ps2.executeQuery();
                while(rs2.next()) {
                    sb.append(String.format("%-15s %-10s %12s\n", 
                        rs2.getString(1), rs2.getString(2), df.format(rs2.getDouble(3))));
                }
                sb.append("------------------------------------------\n");
                sb.append("TỔNG TIỀN THANH TOÁN: ").append(df.format(rs.getDouble("thanhTien"))).append("\n");
                sb.append("==========================================\n");
            }
        } catch (Exception e) {
            sb.append("Lỗi hệ thống: ").append(e.getMessage());
        }

        area.setText(sb.toString());
        dlg.add(new JScrollPane(area), BorderLayout.CENTER);
        
        JButton btnPrint = new JButton("IN HÓA ĐƠN");
        btnPrint.addActionListener(e -> {
            JOptionPane.showMessageDialog(dlg, "Đã in hóa đơn thành công! " + maHD);
            dlg.dispose();
        });
        dlg.add(btnPrint, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    // Các hàm load data và lọc giữ nguyên như cũ (đảm bảo maNV/maHD đúng SQL)
    public void loadDuLieuThanhToanMoi(String maHD) {
        try (Connection con = ConnectDB.getConnection()) {
            String sql = "SELECT h.maHD, k.tenKH, h.thanhTien FROM HoaDon h JOIN KhachHang k ON h.maKH = k.maKH WHERE h.maHD = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtMaHDMoi.setText(rs.getString(1));
                txtTenKHMoi.setText(rs.getString(2));
                txtTongTienMoi.setText(df.format(rs.getDouble(3)));
                btnXacNhan.setEnabled(true);
                
                modelVeChoThanhToan.setRowCount(0);
                PreparedStatement ps2 = con.prepareStatement("SELECT maVe, 'Vé Tàu', donGia FROM ChiTietHoaDon WHERE maHD = ?");
                ps2.setString(1, maHD);
                ResultSet rs2 = ps2.executeQuery();
                while(rs2.next()) modelVeChoThanhToan.addRow(new Object[]{rs2.getString(1), rs2.getString(2), df.format(rs2.getDouble(3))});
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void thanhToanThanhCong() {
        JOptionPane.showMessageDialog(this, "Thanh toán hoàn tất!");
        btnXacNhan.setEnabled(false);
        txtMaHDMoi.setText("");
        modelVeChoThanhToan.setRowCount(0);
        locLichSu();
    }

    private void loadNhanVienToCombo() {
        cbNhanVienLoc.addItem("Tất cả");
        try (Connection con = ConnectDB.getConnection()) {
            ResultSet rs = con.createStatement().executeQuery("SELECT maNV, tenNV FROM NhanVien");
            while (rs.next()) cbNhanVienLoc.addItem(new NhanVienItem(rs.getString(1), rs.getString(2)));
        } catch (Exception e) {}
    }

    private void locLichSu() {
        if (modelLichSu == null) return;
        modelLichSu.setRowCount(0);
        
        try (Connection con = ConnectDB.getConnection()) {
            // 1. Khởi tạo câu SQL cơ bản
            StringBuilder sql = new StringBuilder("SELECT maHD, ngayLap, thanhTien FROM HoaDon WHERE 1=1 ");
            
            // 2. Kiểm tra lọc theo Mã HD (Search box)
            if (!txtTimKiemMaHD.getText().trim().isEmpty()) {
                sql.append(" AND maHD LIKE ? ");
            }
            
            // 3. Kiểm tra lọc theo Nhân viên (ComboBox)
            Object selectedNV = cbNhanVienLoc.getSelectedItem();
            if (selectedNV instanceof NhanVienItem) {
                sql.append(" AND maNV = ? ");
            }
            
            // 4. Kiểm tra lọc theo Ngày lập (JDateChooser)
            if (dateLoc.getDate() != null) {
                sql.append(" AND CAST(ngayLap AS DATE) = ? ");
            }
            
            sql.append(" ORDER BY ngayLap DESC");

            PreparedStatement ps = con.prepareStatement(sql.toString());
            int paramIndex = 1;

            // 5. Gán giá trị cho các tham số theo đúng thứ tự
            if (!txtTimKiemMaHD.getText().trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + txtTimKiemMaHD.getText().trim() + "%");
            }
            
            if (selectedNV instanceof NhanVienItem) {
                ps.setString(paramIndex++, ((NhanVienItem) selectedNV).getMaNV()); // Đảm bảo class NhanVienItem có hàm getMaNV()
            }
            
            if (dateLoc.getDate() != null) {
                // Chuyển đổi từ java.util.Date sang java.sql.Date
                ps.setDate(paramIndex++, new java.sql.Date(dateLoc.getDate().getTime()));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelLichSu.addRow(new Object[]{
                    rs.getString("maHD"), 
                    sdf.format(rs.getTimestamp("ngayLap")), 
                    df.format(rs.getDouble("thanhTien"))
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lọc dữ liệu: " + e.getMessage());
        }
    }
}