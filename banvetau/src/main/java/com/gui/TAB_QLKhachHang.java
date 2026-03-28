package com.gui;

import com.dao.DAO_KhachHang;
import com.entities.KhachHang;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class TAB_QLKhachHang extends JPanel {
	private JTable table;
	private DefaultTableModel model;
	private JTextField txtSearch;
	private JComboBox<String> cbSort;
	private JButton btnPrint;
	private DAO_KhachHang kh_dao = new DAO_KhachHang();

	// Bảng màu hiện đại (Modern UI Palette)
	private final Color PRIMARY_BLUE = new Color(41, 128, 185); // Xanh Navy
	private final Color SUCCESS_GREEN = new Color(39, 174, 96); // Xanh lá (Lưu/Cập nhật)
	private final Color DANGER_RED = new Color(192, 57, 43); // Đỏ (Xóa)
	private final Color BG_LIGHT = new Color(245, 247, 250); // Nền xám nhạt

	public TAB_QLKhachHang() {
		setLayout(new BorderLayout(0, 15));
		setBackground(BG_LIGHT);
		setBorder(new EmptyBorder(20, 25, 20, 25));

		// --- 1. THANH CÔNG CỤ (HEADER) ---
		JPanel pnlHeader = new JPanel(new BorderLayout());
		pnlHeader.setOpaque(false);

		JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		pnlLeft.setOpaque(false);

		txtSearch = new JTextField(25);
		txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtSearch.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(200, 200, 200), 1, true),
				new EmptyBorder(8, 12, 8, 12)));
		txtSearch.setToolTipText("Nhập tên hoặc SĐT để tìm kiếm");

		pnlLeft.add(new JLabel("Tìm khách hàng (Tên/Sdt) 🔍"));
		pnlLeft.add(txtSearch);

		cbSort = new JComboBox<>(new String[] { "Sắp xếp: Mặc định", "Tên khách hàng", "Số điện thoại" });
		cbSort.setPreferredSize(new Dimension(160, 35));
		pnlLeft.add(cbSort);

		pnlHeader.add(pnlLeft, BorderLayout.WEST);

		// --- 2. BẢNG DỮ LIỆU (CENTER) ---
		JPanel pnlTableContainer = new JPanel(new BorderLayout());
		pnlTableContainer.setBackground(Color.WHITE);
		pnlTableContainer.setBorder(new LineBorder(new Color(230, 233, 237), 1, true));

		JLabel lblTitle = new JLabel("  DANH SÁCH KHÁCH HÀNG");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblTitle.setForeground(new Color(52, 73, 94));
		lblTitle.setPreferredSize(new Dimension(0, 50));
		lblTitle.setOpaque(true);
		lblTitle.setBackground(new Color(250, 251, 252));
		lblTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(235, 235, 235)));

		String[] cols = { "Mã KH", "Họ và Tên", "Email", "Số điện thoại", "CCCD" };
		model = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		table = new JTable(model);
		table.setRowHeight(40);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
		table.getTableHeader().setBackground(new Color(240, 242, 245));
		table.setSelectionBackground(new Color(232, 244, 253));
		table.setSelectionForeground(Color.BLACK);
		table.setShowVerticalLines(false);

		pnlTableContainer.add(lblTitle, BorderLayout.NORTH);
		pnlTableContainer.add(new JScrollPane(table), BorderLayout.CENTER);

		// --- 3. NÚT BẤM (SOUTH) ---
		btnPrint = createStyledButton("In danh sách Khách hàng", PRIMARY_BLUE, Color.WHITE);
		JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pnlSouth.setOpaque(false);
		pnlSouth.add(btnPrint);

		add(pnlHeader, BorderLayout.NORTH);
		add(pnlTableContainer, BorderLayout.CENTER);
		add(pnlSouth, BorderLayout.SOUTH);

		initEvents();
		loadData();
	}

	private void initEvents() {
		// Sự kiện tìm kiếm: Gõ tới đâu lọc tới đó
		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				search();
			}

			public void removeUpdate(DocumentEvent e) {
				search();
			}

			public void changedUpdate(DocumentEvent e) {
				search();
			}
		});

		// Double Click để mở Dialog sửa
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
					showEditDialog();
			}
		});
	}

	private void showEditDialog() {
		int row = table.getSelectedRow();
		if (row == -1)
			return;

		// Lấy dữ liệu từ bảng
		String ma = model.getValueAt(row, 0).toString();
		String ten = model.getValueAt(row, 1).toString();
		String email = model.getValueAt(row, 2).toString();
		String sdt = model.getValueAt(row, 3).toString();
		String cccd = model.getValueAt(row, 4).toString();

		JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chỉnh sửa khách hàng", true);
		dialog.setLayout(new GridBagLayout());
		dialog.getContentPane().setBackground(Color.WHITE);
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(10, 15, 10, 15);
		g.fill = GridBagConstraints.HORIZONTAL;

		JTextField tTen = createDialogTextField(ten);
		JTextField tEmail = createDialogTextField(email);
		JTextField tSdt = createDialogTextField(sdt);
		JTextField tCccd = createDialogTextField(cccd);

		// Layout Dialog
		g.gridx = 0;
		g.gridy = 0;
		dialog.add(new JLabel("Mã KH: " + ma), g);
		g.gridx = 0;
		g.gridy = 1;
		dialog.add(new JLabel("Họ tên:"), g);
		g.gridx = 1;
		dialog.add(tTen, g);
		g.gridx = 0;
		g.gridy = 2;
		dialog.add(new JLabel("Số điện thoại:"), g);
		g.gridx = 1;
		dialog.add(tSdt, g);
		g.gridx = 0;
		g.gridy = 3;
		dialog.add(new JLabel("Email:"), g);
		g.gridx = 1;
		dialog.add(tEmail, g);
		g.gridx = 0;
		g.gridy = 4;
		dialog.add(new JLabel("Số CCCD:"), g);
		g.gridx = 1;
		dialog.add(tCccd, g);

		JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
		pBtn.setOpaque(false);
		JButton bUpdate = createStyledButton("Cập nhật", SUCCESS_GREEN, Color.WHITE);
		JButton bDelete = createStyledButton("Xóa", DANGER_RED, Color.WHITE);
		pBtn.add(bDelete);
		pBtn.add(bUpdate);

		g.gridx = 0;
		g.gridy = 5;
		g.gridwidth = 2;
		dialog.add(pBtn, g);

		// Xử lý Cập nhật
		bUpdate.addActionListener(ev -> {
			String strTen = tTen.getText().trim();
			String strEmail = tEmail.getText().trim();
			String strSdt = tSdt.getText().trim();
			String strCccd = tCccd.getText().trim();

			// SỬA LỖI QUAN TRỌNG: Truyền đúng thứ tự Constructor KhachHang(ma, ten, sdt,
			// cccd, email)
			KhachHang kh = new KhachHang(ma, strTen, strSdt, strCccd, strEmail);

			if (kh_dao.updateKhachHang(kh)) {
				JOptionPane.showMessageDialog(dialog, "Đã cập nhật thành công!");
				dialog.dispose();
				loadData();
			} else {
				JOptionPane.showMessageDialog(dialog, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Xử lý Xóa
		bDelete.addActionListener(ev -> {
			int confirm = JOptionPane.showConfirmDialog(dialog, "Bạn có chắc chắn muốn xóa khách hàng này?",
					"Xác nhận xóa", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				if (kh_dao.deleteKhachHang(ma)) {
					JOptionPane.showMessageDialog(dialog, "Đã xóa khách hàng!");
					dialog.dispose();
					loadData();
				} else {
					JOptionPane.showMessageDialog(dialog, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}

	private void search() {
		String key = txtSearch.getText().trim();

		if (key.isEmpty()) {
			loadData();
			return;
		}

		model.setRowCount(0);
		// Sử dụng hàm search từ DAO (có COLLATE bỏ dấu)
		Vector<Vector<Object>> filteredData = kh_dao.searchKhachHang(key);
		for (Vector<Object> row : filteredData) {
			model.addRow(row);
		}
	}

	public void loadData() {
		model.setRowCount(0);
		Vector<Vector<Object>> data = kh_dao.getAllKhachHang();
		for (Vector<Object> row : data) {
			model.addRow(row);
		}
	}

	// --- HÀM HỖ TRỢ GIAO DIỆN ---
	private JButton createStyledButton(String t, Color bg, Color fg) {
		JButton b = new JButton(t);
		b.setBackground(bg);
		b.setForeground(fg);
		b.setFocusPainted(false);
		b.setFont(new Font("Segoe UI", Font.BOLD, 13));
		b.setBorder(new EmptyBorder(8, 20, 8, 20));
		b.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return b;
	}

	private JTextField createDialogTextField(String text) {
		JTextField tf = new JTextField(text, 20);
		tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tf.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(220, 220, 220), 1),
				new EmptyBorder(5, 8, 5, 8)));
		return tf;
	}
}