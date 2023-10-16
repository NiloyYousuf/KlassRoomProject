package com.example.klassroom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/project";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "200041123";

    // Database connection instance
    private static Connection connection = null;

    // Private constructor to prevent creating multiple instances
    private DatabaseConnection() {
    }

    // Get the database connection instance
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Create a new database connection if it doesn't exist
                connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    // Close the database connection

}
