package com.dao;

import java.sql.*;
import com.connectDB.ConnectDB;
import com.entities.NhanVien;
import com.enums.TrangThaiNhanVien;

public class DAO_NhanVien {

	/**
	 * --- HÀM LẤY ĐỐI TƯỢNG NHÂN VIÊN THEO ID --- Dùng để lấy Tên và Chức vụ sau
	 * khi đăng nhập thành công
	 */
	public NhanVien getNhanVienById(String id) {
		NhanVien nv = null;
		String sql = "SELECT * FROM NhanVien WHERE maNV = ?";

		try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

			stmt.setString(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					nv = new NhanVien();
					nv.setMaNV(rs.getString("maNV"));
					nv.setTenNV(rs.getString("tenNV"));
					nv.setChucVu(rs.getString("chucVu"));
					nv.setMatKhau(rs.getString("matKhau"));

					// Xử lý Enum trạng thái
					String statusStr = rs.getString("trangThai");
					nv.setTrangThaiNV(TrangThaiNhanVien.fromString(statusStr));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nv;
	}

	/**
	 * --- HÀM ĐĂNG NHẬP --- Kiểm tra tài khoản, mật khẩu và trạng thái hoạt động
	 */
	public boolean checkLogin(String username, String password) {
		// Lưu ý: Tên cột 'trangThai' phải khớp với DB của bạn (có thể là trangThaiNV)
		String sql = "SELECT maNV FROM NhanVien WHERE maNV = ? AND matKhau = ? AND (trangThai = 'HOATDONG' OR trangThai = 'HOAT DONG')";

		try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

			stmt.setString(1, username);
			stmt.setString(2, password);

			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * --- HÀM ĐĂNG KÝ / TẠO MỚI ---
	 */
	public boolean create(String maNV, String tenNV, String matKhau, String chucVu) {
		String sql = "INSERT INTO NhanVien (maNV, tenNV, matKhau, chucVu, trangThai) VALUES (?, ?, ?, ?, ?)";

		try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

			stmt.setString(1, maNV);
			stmt.setString(2, tenNV);
			stmt.setString(3, matKhau);
			// Chuẩn hóa chức vụ để phân quyền dễ hơn
			stmt.setString(4, chucVu);
			stmt.setString(5, "HOATDONG");

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * --- KIỂM TRA MÃ NV TỒN TẠI ---
	 */
	public boolean isIdExists(String id) {
		String sql = "SELECT COUNT(*) FROM NhanVien WHERE maNV = ?";
		try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

			if (con == null)
				return false;

			stmt.setString(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}