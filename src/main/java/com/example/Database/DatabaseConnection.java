package com.example.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/project";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "200041123";
    public static Connection  connection;

 DatabaseConnection() throws SQLException {connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
 }
    public static Connection getConnection() throws SQLException {
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        return connection;
    }
}

