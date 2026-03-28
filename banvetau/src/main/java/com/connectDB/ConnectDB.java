package com.connectDB;

import java.sql.*;

public class ConnectDB {
	public static Connection getConnection() throws SQLException {
		String url = "jdbc:sqlserver://localhost:1433;databaseName=BanVeTau;encrypt=false;trustServerCertificate=true";
		String user = "sa";
		String password = "sapassword";

		return DriverManager.getConnection(url, user, password);
	}
}