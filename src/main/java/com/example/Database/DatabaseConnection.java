package com.example.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/Project";// I named my database as
                                                                            //"Project" instead of "project"
    private static final String USERNAME = "root";
   // private static final String PASSWORD = "200041116";
    private static final String PASSWORD = "200041123";
    public static Connection  connection;


    private static final String url = "jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12655925";
    private static final String user = "sql12655925";
    private static final  String password = "W45fwGDgsK";

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

