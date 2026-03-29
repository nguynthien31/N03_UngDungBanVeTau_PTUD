package com.dao;

import com.entities.NhanVien;
import com.enums.ChucVu;
import com.enums.TrangThaiNhanVien;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO_NhanVien {

    private Connection conn;

    public DAO_NhanVien(Connection conn) {
        this.conn = conn;
    }

    // ================= GET ALL =================
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien ORDER BY ngayVaoLam ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= GET BY ID =================
    public NhanVien getNhanVienByID(String maNV) {
        String sql = "SELECT * FROM NhanVien WHERE maNV = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================= SEARCH BY TEN =================
    public List<NhanVien> findNhanVienByTenNV(String keyword) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE tenNV LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= INSERT =================

    public boolean insertNhanVien(NhanVien nv) {
        String sql = "INSERT INTO NhanVien(tenNV, sdt, email, taiKhoan, matKhau, chucVu, trangThai, ngayVaoLam) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getTenNV());
            ps.setString(2, nv.getSdt());
            ps.setString(3, nv.getEmail());
            ps.setString(5, nv.getTaiKhoan());
            ps.setString(6, nv.getMatKhau());
            ps.setString(7, nv.getChucVu().name()); // QUANLY
            ps.setString(8, nv.getTrangThai().name()); // HOATDONG
            ps.setDate(9, new java.sql.Date(nv.getNgayVaoLam().getTime()));

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ================= UPDATE =================
    public boolean updateNhanVien(NhanVien nv) {
        String sql = "UPDATE NhanVien SET " +
                "tenNV = ?, sdt = ?, email = ?, taiKhoan = ?, matKhau = ?, chucVu = ?, " +
                "trangThai = ?, ngayVaoLam = ? " +
                "WHERE maNV = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getTenNV());
            ps.setString(2, nv.getSdt());
            ps.setString(3, nv.getEmail());
            ps.setString(4, nv.getTaiKhoan());
            ps.setString(5, nv.getMatKhau());
            ps.setString(6, nv.getChucVu().name());
            ps.setString(7, nv.getTrangThai().name());
            ps.setDate(8, new java.sql.Date(nv.getNgayVaoLam().getTime()));
            ps.setString(9, nv.getMaNV());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= MAP =================
    private NhanVien mapResultSet(ResultSet rs) throws SQLException {
        return new NhanVien(
                rs.getString("maNV"),
                rs.getString("tenNV"),
                rs.getString("sdt"),
                rs.getString("email"),
                rs.getString("taiKhoan"),
                rs.getString("matKhau"),
                ChucVu.fromString(rs.getString("chucVu")),
                TrangThaiNhanVien.fromString(rs.getString("trangThai")),
                rs.getDate("ngayVaoLam")
        );
    }
}