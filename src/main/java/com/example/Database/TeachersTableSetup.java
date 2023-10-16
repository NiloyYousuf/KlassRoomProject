package com.example.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeachersTableSetup {


    public static void main(String[] args) {
        Connection connection = null;
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a database connection
            connection = DatabaseConnection.getConnection();

            // Create the 'teachers' table
            String createTableSQL = "CREATE TABLE IF NOT EXISTS teachers ("
                    + "teacher_username varchar(50) PRIMARY KEY,"
                    + "teacher_password varchar(255)"
                    + ")";

            PreparedStatement createTableStatement = connection.prepareStatement(createTableSQL);
            createTableStatement.execute();

            System.out.println("Table 'teachers' created.");

            // Insert data into the 'teachers' table
            String insertDataSQL = "INSERT INTO teachers (teacher_username, teacher_password) VALUES (?, ?)";

            PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSQL);
            insertDataStatement.setString(1, "teacher2");
            insertDataStatement.setString(2, "123");

            int rowsInserted = insertDataStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data inserted into 'teachers' table.");
            } else {
                System.out.println("Insert failed.");
            }

            // Retrieve and display all entries from the 'teachers' table
            String selectDataSQL = "SELECT * FROM teachers";

            PreparedStatement selectDataStatement = connection.prepareStatement(selectDataSQL);
            ResultSet resultSet = selectDataStatement.executeQuery();

            System.out.println("Entries in 'teachers' table:");
            while (resultSet.next()) {
                String teacherUsername = resultSet.getString("teacher_username");
                String teacherPassword = resultSet.getString("teacher_password");
                System.out.println("Username: " + teacherUsername + ", Password: " + teacherPassword);
            }
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
