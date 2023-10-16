package com.example.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostsTableSetup {

    public static void main(String[] args) {
        try {
            Connection connection =DatabaseConnection.getConnection();

            // Create the posts table
            createPostsTable(connection);

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createPostsTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE posts ("
                + "post_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "classroom_id INT, "
                + "post_time VARCHAR(50), "
                + "post_date VARCHAR(50), "
                + "post_text TEXT, "
                + "attachment LONGBLOB, "
                + "FOREIGN KEY (classroom_id) REFERENCES classrooms(classroom_id)"
                + ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
            preparedStatement.execute();
            System.out.println("posts table created successfully.");
        }
    }
}
