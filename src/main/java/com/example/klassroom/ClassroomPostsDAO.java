package com.example.klassroom;

import java.io.File;
import java.io.FileInputStream;
import java.net.ConnectException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassroomPostsDAO {


    private static final String GET_POST_BY_ID = "SELECT * FROM posts WHERE post_id = ?";


    // JDBC queries
    private static final String INSERT_POST = "INSERT INTO posts (classroom_id, post_time, post_date, post_text, attachment) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_POSTS_BY_CLASSROOM = "SELECT * FROM posts WHERE classroom_id = ? ORDER BY post_date DESC, post_time DESC";



    // Insert a new post into the database
    public static void insertPost(int classroomId, String postTime, String postDate, String postText, byte[] attachment) {
        Connection connection=DatabaseConnection.getConnection();
        DatabaseConnection.establishConnection();
        try {PreparedStatement statement = connection.prepareStatement(INSERT_POST) ;
            statement.setInt(1, classroomId);
            statement.setString(2, postTime);
            statement.setString(3, postDate);
            statement.setString(4, postText);
            statement.setBytes(5, attachment);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert post into the database.");
        }
    }

    // Get posts by classroom ID
    public static List<Post> getPostsByClassroom(int classroomId) {
        List<Post> posts = new ArrayList<>();
        try {
            Connection connection =DatabaseConnection.getConnection();
            DatabaseConnection.establishConnection();
            PreparedStatement statement = connection.prepareStatement(GET_POSTS_BY_CLASSROOM);
            statement.setInt(1, classroomId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int postId = resultSet.getInt("post_id");
                String postTime = resultSet.getString("post_time");
                String postDate = resultSet.getString("post_date");
                String postText = resultSet.getString("post_text");
                byte[] attachment = resultSet.getBytes("attachment");
                Post post = new Post(postId, classroomId, postTime, postDate, postText, attachment);
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve posts from the database.");
        }
        return posts;
    }



    public static Post getPostById(int postId) {
        Connection connection =DatabaseConnection.getConnection();
        try {PreparedStatement statement = connection.prepareStatement(GET_POST_BY_ID);
            statement.setInt(1, postId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int classroomId = resultSet.getInt("classroom_id");
                String postTime = resultSet.getString("post_time");
                String postDate = resultSet.getString("post_date");
                String postText = resultSet.getString("post_text");
                byte[] attachment = resultSet.getBytes("attachment");
                return new Post(postId, classroomId, postTime, postDate, postText, attachment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve the post from the database.");
        }
        return null; // Post not found
    }


    private static final String INSERT_POST2 = "INSERT INTO posts (classroom_id, post_time, post_date, post_text, attachment, original_filename) VALUES (?, ?, ?, ?, ?, ?)";

    public static void insertPost(int classroomId, String postTime, String postDate, String postText, File selectedFile, String originalFilename) {
        Connection connection = DatabaseConnection.getConnection();
        FileInputStream fileInputStream = null;

        try {
            if (selectedFile != null) {
                fileInputStream = new FileInputStream(selectedFile);
            }

            try {PreparedStatement statement = connection.prepareStatement(INSERT_POST2);
                statement.setInt(1, classroomId);
                statement.setString(2, postTime);
                statement.setString(3, postDate);
                statement.setString(4, postText);

                if (fileInputStream != null) {
                    statement.setBinaryStream(5, fileInputStream, (int) selectedFile.length());
                } else {
                    statement.setNull(5, Types.BLOB);
                }

                statement.setString(6, originalFilename);

                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert post into the database.");
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
