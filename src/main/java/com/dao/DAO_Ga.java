package com.dao;



import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.connectDB.ConnectDB;
import com.entities.Ga;

public class DAO_Ga {
    public List<Ga> getAllGa() {
        List<Ga> dsGa = new ArrayList<>();
        String sql = "SELECT * FROM Ga ORDER BY tenGa ASC";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                dsGa.add(new Ga(
                        rs.getString("maGa"),
                        rs.getString("tenGa"),
                        rs.getString("diaChi")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsGa;
    }
}