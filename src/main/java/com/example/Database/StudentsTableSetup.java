package com.example.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentsTableSetup {


    public static void main(String[] args) {
        Connection connection = null;
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a database connection
            connection = DatabaseConnection.getConnection();

            // Create the 'students' table if it doesn't exist
            String createTableSQL = "CREATE TABLE IF NOT EXISTS students ("
                    + "student_username varchar(50) PRIMARY KEY,"
                    + "student_password varchar(255)"
                    + ")";

            PreparedStatement createTableStatement = connection.prepareStatement(createTableSQL);
            createTableStatement.execute();

            // Insert a record into the 'students' table if it doesn't exist
            String insertDataSQL = "INSERT IGNORE INTO students (student_username, student_password) VALUES (?, ?)";
            PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSQL);
            insertDataStatement.setString(1, "student3");
            insertDataStatement.setString(2, "123");
            insertDataStatement.execute();

            // Fetch all data from the 'students' table
            String fetchDataSQL = "SELECT * FROM students";
            PreparedStatement fetchDataStatement = connection.prepareStatement(fetchDataSQL);
            ResultSet resultSet = fetchDataStatement.executeQuery();

            System.out.println("Students Table Data:");
            while (resultSet.next()) {
                String username = resultSet.getString("student_username");
                String password = resultSet.getString("student_password");
                System.out.println("Username: " + username + ", Password: " + password);
            }

            System.out.println("Table 'students' created, record inserted, and data fetched.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
