package com.example.klassroom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClassroomPostsDAO {


    private static final String GET_POST_BY_ID = "SELECT * FROM posts WHERE post_id = ?";
    private static Connection connection=DatabaseConnection.getConnection();

    // JDBC queries
    private static final String INSERT_POST = "INSERT INTO posts (classroom_id, post_time, post_date, post_text, attachment) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_POSTS_BY_CLASSROOM = "SELECT * FROM posts WHERE classroom_id = ?";


    // Insert a new post into the database
    public static void insertPost(int classroomId, String postTime, String postDate, String postText, byte[] attachment) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_POST)) {
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
        try (PreparedStatement statement = connection.prepareStatement(GET_POSTS_BY_CLASSROOM)) {
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

    public static void printAllPosts(int classroomId) {
        List<Post> posts = getPostsByClassroom(classroomId);
        System.out.println("Posts for Classroom ID " + classroomId + ":");
        for (Post post : posts) {
            System.out.println("Post ID: " + post.getPostId());
            System.out.println("Post Time: " + post.getPostTime());
            System.out.println("Post Date: " + post.getPostDate());
            System.out.println("Post Text: " + post.getPostText());
            // You can handle attachments here if needed
            System.out.println("---------------");
        }
    }

    public static Post getPostById(int postId) {
        try (PreparedStatement statement = connection.prepareStatement(GET_POST_BY_ID)) {
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



}
