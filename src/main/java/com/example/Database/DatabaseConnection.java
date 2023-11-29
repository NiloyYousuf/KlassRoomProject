package com.example.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
//    private static final String url = "jdbc:mysql://localhost:3306/project";// I named my database as
//                                                                            //"Project" instead of "project"
//    private static final String user = "root";
//   // private static final String PASSWORD = "200041116";
//    private static final String password = "200041123";
    public static Connection  connection;



    private static final String url = "jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12666169";
    private static final String user = "sql12666169";
    private static final String password = "dZmh6WCGlT";
 DatabaseConnection() throws SQLException {
     //connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
     connection = DriverManager.getConnection(url, user, password);
 }
    public static Connection getConnection() throws SQLException {
      //  connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        connection = DriverManager.getConnection(url, user, password);
        return connection;
    }
}

