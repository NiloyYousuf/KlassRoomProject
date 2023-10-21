package com.example.Database;
import com.example.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.Database.DatabaseConnection.connection;

public class CreateTableAssignments {
    public static void main(String[] args) throws SQLException {
        // JDBC URL, username, and password of MySQL server
       // Replace with your database password

        try (Connection connection = DatabaseConnection.getConnection()) {
            Statement statement = connection.createStatement();

            // Define the SQL query to create the assignments table with a BLOB for attachments
            String createTableSQL = "CREATE TABLE assignments (" +
                    "Assignment_ID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "Assignment_Text TEXT, " +
                    "Assign_Date DATE, " +
                    "Deadline DATE, " +
                    "Attachment LONGBLOB, " + // Use BLOB data type for file attachments
                    "Marks INT, " +
                    "Teacher_Username VARCHAR(50), " +
                    "Classroom_Code VARCHAR(10), " +
                    "FOREIGN KEY (Teacher_Username) REFERENCES teachers(teacher_username), " +
                    "FOREIGN KEY (Classroom_Code) REFERENCES classrooms(classroom_code)" +
                    ")";

            // Execute the SQL query to create the table
            statement.execute(createTableSQL);

            System.out.println("Assignments table created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
