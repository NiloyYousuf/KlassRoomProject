package com.example.Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateCommentTable {

    public static void main(String[] args) {


        try {
            Connection connection = DatabaseConnection.getConnection();

            if (connection != null) {
                System.out.println("Connected to the database");

                // Create 'Comments' table with foreign key constraints
                String createTableSQL = "CREATE TABLE Comments ("
                        + "commentID INT AUTO_INCREMENT PRIMARY KEY,"
                        + "commentText TEXT NOT NULL,"
                        + "commentTime TIME NOT NULL,"
                        + "CommentDate DATE NOT NULL,"
                        + "postID INT NOT NULL,"
                        + "student_username VARCHAR(50),"
                        + "teacher_username VARCHAR(50),"
                        + "FOREIGN KEY (postID) REFERENCES posts(post_id),"
                        + "FOREIGN KEY (student_username) REFERENCES students(student_username),"
                        + "FOREIGN KEY (teacher_username) REFERENCES teachers(teacher_username)"
                        + ")";

                Statement statement = connection.createStatement();
                statement.execute(createTableSQL);

                System.out.println("Table 'Comments' created successfully with foreign key constraints.");

                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
