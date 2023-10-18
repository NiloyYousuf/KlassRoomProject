package com.example.klassroom;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TeacherPostController implements Initializable {

    @FXML
    private Label Date;

    @FXML
    private Label Time;

    @FXML
    private TextArea Post;

    @FXML
    private Button Download;

    @FXML
    private VBox commentVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Date.setText(TeacherPostTileController.current_post.getPostDate());
        Time.setText(TeacherPostTileController.current_post.getPostTime());
        Post.setText(TeacherPostTileController.current_post.getPostText());
        System.out.println(TeacherPostTileController.current_post.getPostText() + " This is the text of the comment");
        System.out.println("Debug current post ID  " + currentPost.currentPostId + TeacherPostTileController.current_post.getPostId());
        // Fetch and print comments for the current post
        printCommentsForPost(TeacherPostTileController.current_post.getPostId());
        loadCommentSection();


    }


    public void loadCommentSection() {
        try {
            // Load the CommentSection.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CommentSection.fxml"));
            AnchorPane commentSection = loader.load();

            // Add the CommentSection to the commentVBox
            commentVBox.getChildren().add(commentSection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




        public void Go_back()
    {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ClassroomTeacher.fxml"));
            Parent root = loader.load();

            // Get the current stage (assuming you have a reference to the current stage)
            Stage stage = (Stage) Download.getScene().getWindow();

            // Set the new FXML content on the current stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    public void printCommentsForPost(int postId) {
        try {
            // Replace with your database connection details
            Connection connection = DatabaseConnection.getConnection();
            // SQL query to fetch comments for the given postID
            String sql = "SELECT commentID, commentText, commentTime, CommentDate, student_username, teacher_username FROM Comments WHERE postID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, postId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int commentID = resultSet.getInt("commentID");
                String commentText = resultSet.getString("commentText");
                String commentTime = resultSet.getString("commentTime");
                String commentDate = resultSet.getString("CommentDate");
                String studentUsername = resultSet.getString("student_username");
                String teacherUsername = resultSet.getString("teacher_username");

                System.out.println("Comment ID: " + commentID);
                System.out.println("Comment Text: " + commentText);
                System.out.println("Comment Time: " + commentTime);
                System.out.println("Comment Date: " + commentDate);
                System.out.println("Student Username: " + studentUsername);
                System.out.println("Teacher Username: " + teacherUsername);
                System.out.println(); // Add a separator between comments
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

