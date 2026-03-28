package com.gui;

import com.dao.DAO_Tau;
import com.entities.Tau;
import com.enums.TrangThaiTau;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class TAB_Tau extends JPanel {
	// --- Khai báo các thành phần giao diện ---
	private JTable table;
	private DefaultTableModel model;
	private JTextField txtSearch;
	private JButton btnAdd, btnDelete, btnUpdate, btnRefresh, btnSearch;

	// --- Khai báo lớp xử lý dữ liệu ---
	private DAO_Tau DAO_Tau = new DAO_Tau();
	private Color primaryColor = new Color(0, 122, 255);

	public TAB_Tau() {
		setLayout(new BorderLayout(0, 15));
		setBackground(Color.WHITE);
		setBorder(new EmptyBorder(20, 20, 20, 20));

		initUI(); // Khởi tạo giao diện
		initEvents(); // Khởi tạo sự kiện
		loadDataFromDatabase(); // Đổ dữ liệu lần đầu
	}

	private void initUI() {
		// 1. TOP PANEL (Tiêu đề & Thanh công cụ)
		JPanel pnlTop = new JPanel(new BorderLayout());
		pnlTop.setOpaque(false);

		JLabel lblTitle = new JLabel("QUẢN LÝ ĐOÀN TÀU");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblTitle.setForeground(primaryColor);
		pnlTop.add(lblTitle, BorderLayout.NORTH);

		// Thanh công cụ
		JPanel pnlActions = new JPanel(new BorderLayout(10, 0));
		pnlActions.setOpaque(false);
		pnlActions.setBorder(new EmptyBorder(15, 0, 10, 0));

		// Cụm tìm kiếm bên trái
		JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pnlSearch.setOpaque(false);
		txtSearch = new JTextField(25);
		txtSearch.setPreferredSize(new Dimension(0, 35));
		btnSearch = createStyledButton("Tìm kiếm", primaryColor, true);
		pnlSearch.add(txtSearch);
		pnlSearch.add(Box.createHorizontalStrut(5));
		pnlSearch.add(btnSearch);

		// Cụm nút bấm bên phải
		JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		pnlButtons.setOpaque(false);
		btnAdd = createStyledButton("Thêm mới", new Color(40, 167, 69), false);
		btnDelete = createStyledButton("Xóa", new Color(220, 53, 69), false);
		btnUpdate = createStyledButton("Cập nhật", new Color(255, 193, 7), false);
		btnRefresh = createStyledButton("Làm mới", new Color(108, 117, 125), false);

		pnlButtons.add(btnAdd);
		pnlButtons.add(btnDelete);
		pnlButtons.add(btnUpdate);
		pnlButtons.add(btnRefresh);

		pnlActions.add(pnlSearch, BorderLayout.WEST);
		pnlActions.add(pnlButtons, BorderLayout.EAST);
		pnlTop.add(pnlActions, BorderLayout.CENTER);
		add(pnlTop, BorderLayout.NORTH);

		// 2. CENTER PANEL (Bảng dữ liệu)
		String[] columns = { "STT", "Mã Tàu", "Tên Tàu", "Số Toa", "Trạng thái" };
		model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		setupTableAppearance();

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
		add(scrollPane, BorderLayout.CENTER);
	}

	private void setupTableAppearance() {
		JTableHeader header = table.getTableHeader();
		header.setFont(new Font("Segoe UI", Font.BOLD, 13));
		header.setBackground(new Color(245, 245, 245));
		header.setPreferredSize(new Dimension(0, 40));

		table.setRowHeight(35);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		table.setSelectionBackground(new Color(230, 240, 255));
		table.setSelectionForeground(Color.BLACK);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
	}

	private void initEvents() {
		// --- THÊM MỚI ---
		btnAdd.addActionListener(e -> {
			Form_Tau form = new Form_Tau((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Tàu Mới");
			form.setVisible(true);
			if (form.isConfirmed()) {
				if (DAO_Tau.insertTau(form.getEntity())) {
					loadDataFromDatabase();
					JOptionPane.showMessageDialog(this, "Thêm thành công!");
				} else {
					JOptionPane.showMessageDialog(this, "Lỗi: Mã tàu trùng hoặc sai định dạng!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// --- CẬP NHẬT ---
		btnUpdate.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn tàu cần sửa!");
				return;
			}
			String ma = table.getValueAt(row, 1).toString();
			String ten = table.getValueAt(row, 2).toString();
			int soToa = Integer.parseInt(table.getValueAt(row, 3).toString());
			String moTa = table.getValueAt(row, 4).toString();

			TrangThaiTau tt = TrangThaiTau.HOATDONG;
			for (TrangThaiTau t : TrangThaiTau.values()) {
				if (t.getMoTa().equals(moTa)) {
					tt = t;
					break;
				}
			}

			Form_Tau form = new Form_Tau((Frame) SwingUtilities.getWindowAncestor(this), "Cập Nhật Tàu");
			form.setEntity(new Tau(ma, ten, soToa, tt));
			form.setVisible(true);

			if (form.isConfirmed()) {
				if (DAO_Tau.updateTau(form.getEntity())) {
					loadDataFromDatabase();
					JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
				}
			}
		});

		// --- XÓA ---
		btnDelete.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(this, "Chọn tàu để xóa!");
				return;
			}
			String ma = table.getValueAt(row, 1).toString();
			if (JOptionPane.showConfirmDialog(this, "Xóa tàu " + ma + "?", "Xác nhận",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				if (DAO_Tau.deleteTau(ma)) {
					loadDataFromDatabase();
					JOptionPane.showMessageDialog(this, "Đã xóa!");
				} else {
					JOptionPane.showMessageDialog(this, "Không thể xóa do ràng buộc dữ liệu!", "Lỗi",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// --- LÀM MỚI ---
		btnRefresh.addActionListener(e -> {
			txtSearch.setText("");
			loadDataFromDatabase();
		});

		// --- TÌM KIẾM ---
		btnSearch.addActionListener(e -> performSearch());
		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				performSearch();
			}
		});
	}

	private void performSearch() {
		String key = txtSearch.getText().trim();
		List<Tau> list = DAO_Tau.searchTau(key);
		renderTable(list);
	}

	private void loadDataFromDatabase() {
		renderTable(DAO_Tau.getAllTau());
	}

	private void renderTable(List<Tau> list) {
		model.setRowCount(0);
		int stt = 1;
		for (Tau t : list) {
			model.addRow(
					new Object[] { stt++, t.getMaTau(), t.getTenTau(), t.getSoToa(), t.getTrangThaiTau().getMoTa() });
		}
	}

	private JButton createStyledButton(String text, Color color, boolean isSmall) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btn.setBackground(color);
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setPreferredSize(new Dimension(isSmall ? 100 : 110, 35));
		btn.setBorder(BorderFactory.createEmptyBorder());
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return btn;
	}
}