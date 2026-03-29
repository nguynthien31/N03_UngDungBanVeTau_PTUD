package com.connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private static final String URL =
            "jdbc:sqlserver://LAPTOP-KQ2N5H69:1433;" +
                    "databaseName=BanVeTau;" +
                    "encrypt=false;" +
                    "trustServerCertificate=true";

    private static final String USER = "sa";
    private static final String PASSWORD = "123456";

    private static Connection conn = null;

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println(" Kết nối thành công!");
            }
        } catch (SQLException e) {
            System.out.println(" Kết nối thất bại: " + e.getMessage());
        }
        return conn;
    }
}