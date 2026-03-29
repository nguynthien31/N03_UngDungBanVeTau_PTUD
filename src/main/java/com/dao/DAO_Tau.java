package com.dao;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.connectDB.ConnectDB;
import com.entities.Tau;
import com.enums.TrangThaiTau;

public class DAO_Tau {
    public List<Tau> getAllTau() {
        List<Tau> dsTau = new ArrayList<>();
        String sql = "SELECT * FROM Tau";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Chuyển từ String trong DB sang Enum trong Java
                String dbStatus = rs.getString("trangThai");
                TrangThaiTau statusEnum = TrangThaiTau.valueOf(dbStatus);

                Tau t = new Tau(
                        rs.getString("maTau"),
                        rs.getString("tenTau"),
                        rs.getInt("soToa"),
                        statusEnum
                );
                dsTau.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi: Giá trị trạng thái trong DB không khớp với Enum Java!");
        }
        return dsTau;
    }

    public boolean insertTau(Tau t) {
        String sql = "INSERT INTO Tau VALUES(?, ?, ?, ?)";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getMaTau());
            ps.setString(2, t.getTenTau());
            ps.setInt(3, t.getSoToa());
            ps.setString(4, t.getTrangThaiTau().name()); // Chuyển Enum thành String để lưu vào DB
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTau(Tau t) {
        String sql = "UPDATE Tau SET tenTau = ?, soToa = ?, trangThai = ? WHERE maTau = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getTenTau());
            ps.setInt(2, t.getSoToa());
            ps.setString(3, t.getTrangThaiTau().name());
            ps.setString(4, t.getMaTau());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTau(String maTau) {
        String sql = "DELETE FROM Tau WHERE maTau = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTau);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // TÌM KIẾM (Theo Mã hoặc Tên)
    public List<Tau> searchTau(String keyword) {
        List<Tau> list = new ArrayList<>();
        String sql = "SELECT * FROM Tau WHERE maTau LIKE ? OR tenTau LIKE ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Tau(
                        rs.getString("maTau"),
                        rs.getString("tenTau"),
                        rs.getInt("soToa"),
                        TrangThaiTau.valueOf(rs.getString("trangThai"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}