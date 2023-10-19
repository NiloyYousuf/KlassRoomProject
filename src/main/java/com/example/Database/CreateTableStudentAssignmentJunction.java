package com.example.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class CreateTableStudentAssignmentJunction {
    public static void main(String[] args) {
        // JDBC URL, username, and password of MySQL server


        try (Connection connection = DatabaseConnection.getConnection()) {
            Statement statement = connection.createStatement();

            // Define the SQL query to create the student assignment junction table with a BLOB for uploaded assignments
            String createTableSQL = "CREATE TABLE student_assignment_junction (" +
                    "Assignment_ID INT, " +
                    "Student_Username VARCHAR(50), " +
                    "Submission_Date DATE, " +
                    "Submission_Status VARCHAR(50), " +
                    "Marks_Obtained INT, " +
                    "Uploaded_Assignment BLOB, " + // Use BLOB data type for uploaded assignments
                    "PRIMARY KEY (Assignment_ID, Student_Username), " +
                    "FOREIGN KEY (Assignment_ID) REFERENCES assignments(Assignment_ID), " +
                    "FOREIGN KEY (Student_Username) REFERENCES students(student_username)" +
                    ")";

            // Execute the SQL query to create the table
            statement.execute(createTableSQL);

            System.out.println("Student Assignment Junction table created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
