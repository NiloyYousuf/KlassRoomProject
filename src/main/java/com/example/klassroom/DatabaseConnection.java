package com.example.klassroom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DatabaseConnection {
//    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/project";
//    private static final String USERNAME = "root";
//    private static final String PASSWORD = "200041123";

    private static final String JDBC_URL = "jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12655925";
    private static final String USERNAME = "sql12655925";
    private static final String PASSWORD = "W45fwGDgsK";





    // Database connection instance
    private static Connection connection = null;

    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private static final long CONNECTION_CHECK_INTERVAL = 1; // Adjust this interval as needed (in seconds)

    private DatabaseConnection() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            establishConnection(); // Establish the initial connection
            scheduleConnectionCheck(); // Schedule periodic connection checks
        }
        return connection;
    }

    public static void establishConnection() {
        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void scheduleConnectionCheck() {
        executorService.scheduleAtFixedRate(() -> {
            try {
                if (connection == null || connection.isClosed()) {
                    System.out.println("Reconnecting to the database...");
                    establishConnection();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, 0, CONNECTION_CHECK_INTERVAL, TimeUnit.SECONDS);
    }
}
