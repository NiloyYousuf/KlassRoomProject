package com.example.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClassroomsTableSetup {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a database connection
            connection =DatabaseConnection.getConnection();

            // Create the 'classrooms' table
            String createTableSQL = "CREATE TABLE IF NOT EXISTS classrooms ("
                    + "classroom_id int AUTO_INCREMENT PRIMARY KEY,"
                    + "teacher_username varchar(50),"
                    + "classroom_code varchar(10) UNIQUE,"
                    + "classroom_password varchar(10),"
                    + "subject_name varchar(50),"
                    + "FOREIGN KEY (teacher_username) REFERENCES teachers(teacher_username)"
                    + ")";

            PreparedStatement createTableStatement = connection.prepareStatement(createTableSQL);
            createTableStatement.execute();

            System.out.println("Table 'classrooms' created.");

            // Insert data into the 'classrooms' table
            String insertDataSQL = "INSERT INTO classrooms (teacher_username, classroom_code, classroom_password, subject_name) VALUES (?, ?, ?, ?)";

            PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSQL);
            insertDataStatement.setString(1, "teacher1");
            insertDataStatement.setString(2, "C104");
            insertDataStatement.setString(3, "123");
            insertDataStatement.setString(4, "Operating Systems");

            int rowsInserted = insertDataStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data inserted into 'classrooms' table.");
            } else {
                System.out.println("Insert failed.");
            }

            // Retrieve and display all entries from the 'classrooms' table
            String selectDataSQL = "SELECT * FROM classrooms";

            PreparedStatement selectDataStatement = connection.prepareStatement(selectDataSQL);
            ResultSet resultSet = selectDataStatement.executeQuery();

            System.out.println("Entries in 'classrooms' table:");
            while (resultSet.next()) {
                int classroomId = resultSet.getInt("classroom_id");
                String teacherUsername = resultSet.getString("teacher_username");
                String classroomCode = resultSet.getString("classroom_code");
                String classroomPassword = resultSet.getString("classroom_password");
                String subjectName = resultSet.getString("subject_name");
                System.out.println("Classroom ID: " + classroomId + ", Teacher: " + teacherUsername +
                        ", Code: " + classroomCode + ", Password: " + classroomPassword +
                        ", Subject: " + subjectName);
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
