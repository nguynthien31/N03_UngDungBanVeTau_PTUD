package com.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TAB_Ga_Tuyen extends JPanel {

	// Khai báo biến tableTuyen ở cấp class để các hàm khác có thể gọi được
	private JTable tableTuyen;

	public TAB_Ga_Tuyen() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// Tiêu đề
		JLabel lblTitle = new JLabel("QUẢN LÝ GA VÀ TUYẾN ĐƯỜNG", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitle.setForeground(new Color(0, 122, 255));
		lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		add(lblTitle, BorderLayout.NORTH);

		// ================= TẠO SPLIT PANE =================
		JPanel leftPanel_Ga = createGaPanel();
		JPanel rightPanel_Tuyen = createTuyenPanel();

		// JSplitPane chia màn hình làm 2 theo chiều ngang
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel_Ga, rightPanel_Tuyen);
		splitPane.setDividerLocation(450); // Set độ rộng ban đầu cho khung bên trái
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);

		add(splitPane, BorderLayout.CENTER);
	}

	// ================= KHUNG BÊN TRÁI: CHỈ HIỂN THỊ DANH SÁCH GA =================
	private JPanel createGaPanel() {
		// Sử dụng BorderLayout với khoảng cách dọc 10px
		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setBorder(BorderFactory.createTitledBorder("Danh sách Ga (Từ CSDL)"));
		panel.setBackground(Color.WHITE);

		// 1. Thanh tìm kiếm nhanh (Tối ưu UX)
		JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
		searchPanel.setBackground(Color.WHITE);
		searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		searchPanel.add(new JLabel("Tìm kiếm: "), BorderLayout.WEST);
		JTextField txtSearchGa = new JTextField();
		searchPanel.add(txtSearchGa, BorderLayout.CENTER);

		JButton btnSearchGa = new JButton("Lọc");
		btnSearchGa.setBackground(new Color(0, 122, 255));
		btnSearchGa.setForeground(Color.WHITE);
		searchPanel.add(btnSearchGa, BorderLayout.EAST);

		// Đặt thanh tìm kiếm lên đầu
		panel.add(searchPanel, BorderLayout.NORTH);

		// 2. Bảng dữ liệu Ga (Chỉ đọc/Read-only)
		String[] cols = { "Mã Ga", "Tên Ga", "Mô tả" };

		// Ghi đè DefaultTableModel để khóa không cho edit trực tiếp trên ô (cell)
		DefaultTableModel model = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		JTable tableGa = new JTable(model);
		tableGa.setRowHeight(25); // Chỉnh độ cao dòng cho dễ nhìn
		tableGa.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12)); // In đậm tiêu đề cột
		tableGa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Chỉ cho chọn 1 dòng mỗi lần

		// Giả lập dữ liệu Ga
		model.addRow(new Object[] { "G01", "Sài Gòn", "Ga trung tâm TP.HCM" });
		model.addRow(new Object[] { "G02", "Hà Nội", "Ga trung tâm Hà Nội" });
		model.addRow(new Object[] { "G03", "Nha Trang", "Ga hành khách Khánh Hòa" });
		model.addRow(new Object[] { "G04", "Đà Nẵng", "Ga hành khách Đà Nẵng" });

		JScrollPane scrollPane = new JScrollPane(tableGa);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	// ================= KHUNG BÊN PHẢI: QUẢN LÝ TUYẾN =================
	private JPanel createTuyenPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Thông tin Tuyến & Ga trung gian"));
		panel.setBackground(Color.WHITE);

		// 1. Form nhập liệu Tuyến
		JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
		formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		formPanel.setBackground(Color.WHITE);

		formPanel.add(new JLabel("Mã Tuyến:"));
		JTextField txtMaTuyen = new JTextField();
		formPanel.add(txtMaTuyen);

		formPanel.add(new JLabel("Ga Đi:"));
		JComboBox<String> cbGaDi = new JComboBox<>(new String[] { "Chọn Ga", "Sài Gòn", "Hà Nội", "Đà Nẵng" });
		formPanel.add(cbGaDi);

		formPanel.add(new JLabel("Ga Đến:"));
		JComboBox<String> cbGaDen = new JComboBox<>(new String[] { "Chọn Ga", "Nha Trang", "Đà Nẵng", "Hà Nội" });
		formPanel.add(cbGaDen);

		// 2. Nút chức năng Tuyến
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		btnPanel.setBackground(Color.WHITE);

		JButton btnLuu = new JButton("Lưu Tuyến");
		JButton btnChiTiet = new JButton("Chi tiết Ga trung gian");
		JButton btnSua = new JButton("Sửa Tuyến");

		btnPanel.add(btnLuu);
		btnPanel.add(btnChiTiet);
		btnPanel.add(btnSua);

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(formPanel, BorderLayout.CENTER);
		topPanel.add(btnPanel, BorderLayout.SOUTH);
		panel.add(topPanel, BorderLayout.NORTH);

		// 3. Bảng danh sách Tuyến
		String[] cols = { "Mã Tuyến", "Ga Đi", "Ga Đến" };
		DefaultTableModel model = new DefaultTableModel(cols, 0);

		// Khởi tạo tableTuyen
		tableTuyen = new JTable(model);
		tableTuyen.setRowHeight(25);
		tableTuyen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Giả lập dữ liệu Tuyến để test nút Chi tiết
		model.addRow(new Object[] { "T001", "Sài Gòn", "Hà Nội" });
		model.addRow(new Object[] { "T002", "Đà Nẵng", "Nha Trang" });

		JScrollPane scrollPane = new JScrollPane(tableTuyen);
		panel.add(scrollPane, BorderLayout.CENTER);

		// ================= SỰ KIỆN NÚT CHI TIẾT GA TRUNG GIAN =================
		btnChiTiet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = tableTuyen.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(panel, "Vui lòng chọn một Tuyến trong bảng để xem chi tiết!",
							"Cảnh báo", JOptionPane.WARNING_MESSAGE);
				} else {
					// Lấy Mã Tuyến từ dòng đang chọn (Cột 0)
					String maTuyen = (String) tableTuyen.getValueAt(selectedRow, 0);
					// Mở Popup
					hienThiPopupGaTrungGian(maTuyen);
				}
			}
		});

		return panel;
	}

	// ================= HÀM HIỂN THỊ POPUP GA TRUNG GIAN =================
	private void hienThiPopupGaTrungGian(String maTuyen) {
		// 1. Khởi tạo JDialog
		Window parentWindow = SwingUtilities.getWindowAncestor(this);
		JDialog dialog = new JDialog(parentWindow, "Chi tiết Ga trung gian - Tuyến: " + maTuyen,
				JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setSize(600, 450);
		dialog.setLayout(new BorderLayout());
		dialog.setLocationRelativeTo(this); // Hiển thị ở giữa màn hình

		// 2. Khu vực Form nhập liệu (Phía trên)
		JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
		formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		formPanel.add(new JLabel("Chọn Ga trung gian:"));
		JComboBox<String> cbGa = new JComboBox<>(new String[] { "Nha Trang", "Vinh", "Huế", "Đà Nẵng" }); // Tương lai
																											// đổ từ
																											// CSDL
		formPanel.add(cbGa);

		formPanel.add(new JLabel("Thứ tự dừng:"));
		JTextField txtThuTu = new JTextField();
		formPanel.add(txtThuTu);

		formPanel.add(new JLabel("Thời gian dừng (phút):"));
		JTextField txtThoiGian = new JTextField();
		formPanel.add(txtThoiGian);

		// 3. Khu vực Nút bấm
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton btnAdd = new JButton("Thêm Ga");
		JButton btnDelete = new JButton("Xóa Ga");
		btnPanel.add(btnAdd);
		btnPanel.add(btnDelete);

		// Ghép Form và Nút vào một Panel chứa (Top Panel)
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(formPanel, BorderLayout.CENTER);
		topPanel.add(btnPanel, BorderLayout.SOUTH);
		dialog.add(topPanel, BorderLayout.NORTH);

		// 4. Khu vực Bảng hiển thị (Phía dưới)
		String[] cols = { "Tên Ga", "Thứ tự dừng", "Thời gian dừng (Phút)" };
		DefaultTableModel model = new DefaultTableModel(cols, 0);

		// Giả lập có sẵn 1 dòng dữ liệu
		model.addRow(new Object[] { "Nha Trang", "1", "15" });

		JTable tableChiTiet = new JTable(model);
		tableChiTiet.setRowHeight(25);
		JScrollPane scrollPane = new JScrollPane(tableChiTiet);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách các Ga dừng chân"));

		dialog.add(scrollPane, BorderLayout.CENTER);

		// Hiển thị Dialog
		dialog.setVisible(true);
	}
}