package com.example.klassroom;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;

public class CommentSectionController {
    @FXML
    private VBox commentVBox;

    @FXML
    private TextArea newCommentTextArea;

    @FXML
    private Button postButton;

    public void initialize() {


        try  {
            // Fetch comments from the database based on the current post ID
            String query = "SELECT commentText, commentDate, commentTime FROM comments WHERE postID = ?";
            DatabaseConnection.establishConnection();
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, currentPost.currentPostId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String commentText = resultSet.getString("commentText");
                String commentDate = resultSet.getString("commentDate");
                String commentTime = resultSet.getString("commentTime");
                addComment(commentText, commentDate, commentTime);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }


        postButton.setOnAction(event -> {
            // Get the comment text from the TextArea
            String commentText = newCommentTextArea.getText();

            if (!commentText.isEmpty()) {
                // Get the current date and time
                LocalDate currentDate = LocalDate.now();
                LocalTime currentTime = LocalTime.now();
                String commentDate = currentDate.toString();
                String commentTime = currentTime.toString();

                // Get the postID from the current post

                int postID ;

                // Determine whether the comment is made by a student or teacher
                boolean isStudent = Current_User.is_student; // Replace with your logic to determine the user
                if(isStudent)
                 postID = StudentPostTileController.current_post.getPostId();
                else
                    postID=TeacherPostTileController.current_post.getPostId();

                // Insert a new comment record into the Comments table
                insertComment(commentText, commentDate, commentTime, postID, isStudent);

                // Clear the comment text area after posting
                newCommentTextArea.clear();
            }
        });
    }
    public void addComment(String commentText ,String commentDate , String commentTime) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Comment.fxml"));
            HBox commentLayout = loader.load();
            CommentController commentController = loader.getController();
            commentController.setCommentText(commentText);
            commentController.setCommentDateText(commentDate);
            commentController.setCommentTimeText(commentTime);
            commentVBox.getChildren().add(commentLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER && !newCommentTextArea.getText().isEmpty()) {
          // addComment(newCommentTextArea.getText(),new);
            newCommentTextArea.clear();
        }
    }


    private void insertComment(String commentText, String commentDate, String commentTime, int postID, boolean isStudent) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO Comments (commentText, commentTime, CommentDate, postID, student_username, teacher_username) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Determine student or teacher username based on the isStudent variable
            String studentUsername = isStudent ? CurrentStudent.CurrentStudentUsername : null;
            String teacherUsername = isStudent ? null : CurrentTeacher.current_teacher_username;

            preparedStatement.setString(1, commentText);
            preparedStatement.setString(2, commentTime);
            preparedStatement.setString(3, commentDate);
            preparedStatement.setInt(4, postID);
            preparedStatement.setString(5, studentUsername);
            preparedStatement.setString(6, teacherUsername);

            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        newCommentTextArea.clear();

// Refresh the comment section
        refreshCommentSection();
    }

    private void refreshCommentSection() {
        commentVBox.getChildren().clear(); // Clear existing comments
         DatabaseConnection.establishConnection();
        // Fetch and display comments for the current post
        try {
            // Fetch comments from the database based on the current post ID
            String query = "SELECT commentText, commentDate, commentTime FROM comments WHERE postID = ?";
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, currentPost.currentPostId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String commentText = resultSet.getString("commentText");
                String commentDate = resultSet.getString("commentDate");
                String commentTime = resultSet.getString("commentTime");
                addComment(commentText, commentDate, commentTime);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
