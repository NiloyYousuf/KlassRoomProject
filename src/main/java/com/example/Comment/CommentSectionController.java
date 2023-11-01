package com.example.Comment;
import com.example.Current_Variables.Current_User;
import com.example.Current_Variables.currentPost;
import com.example.klassroom.DatabaseConnection;
import com.example.student.CurrentStudent;
import com.example.student.StudentPostTileController;
import com.example.teacher.CurrentTeacher;
import com.example.teacher.TeacherPostTileController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CommentSectionController {
    @FXML
    private VBox commentVBox;

    @FXML
    private TextArea newCommentTextArea;

    @FXML
    private Button postButton;

    public void initialize() {
        try {
            // Fetch comments from the database based on the current post ID
            String query = "SELECT commentID, commentText, commentDate, commentTime, student_username, teacher_username FROM comments WHERE postID = ?";
            DatabaseConnection.establishConnection();
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, currentPost.currentPostId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String commentText = resultSet.getString("commentText");
                String commentDate = resultSet.getString("commentDate");
                String commentTime = resultSet.getString("commentTime");
                Blob userPhotoBlob = getUserPhotoBlob(resultSet);

                // Determine the user's name
                String userName = "";
                String studentUsername = resultSet.getString("student_username");
                String teacherUsername = resultSet.getString("teacher_username");
                if (studentUsername != null) {
                    userName = studentUsername;
                } else if (teacherUsername != null) {
                    userName = teacherUsername;
                }

                addComment(commentText, commentDate, commentTime, userName, userPhotoBlob);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        postButton.setOnAction(event -> {
            // Get the comment text from the TextArea
            String commentText = newCommentTextArea.getText();

            if (!commentText.isEmpty()) {
                // Get the current date and time
                // Format the current date
                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String commentDate = currentDate.format(dateFormatter);

// Format the current time
                LocalTime currentTime = LocalTime.now();
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                String commentTime = currentTime.format(timeFormatter);


                // Get the postID from the current post

                int postID ;

                // Determine whether the comment is made by a student or teacher
                boolean isStudent = Current_User.is_student; // Replace with your logic to determine the user
                if(isStudent)
                 postID = StudentPostTileController.current_post.getPostId();
                else
                    postID= TeacherPostTileController.current_post.getPostId();

                // Insert a new comment record into the Comments table
                DatabaseConnection.establishConnection();
                insertComment(commentText, commentDate, commentTime, postID, isStudent);

                // Clear the comment text area after posting
                newCommentTextArea.clear();
            }
        });
    }
    public void addComment(String commentText, String commentDate, String commentTime, String user_name, Blob userPhoto) {
        try {
            FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Comment/comment.fxml").toURL());
            HBox commentLayout = loader.load();
            CommentController commentController = loader.getController();
            commentController.setCommentUsername(user_name);
            commentController.setCommentText(commentText);
            commentController.setCommentDateText(commentDate);
            commentController.setCommentTimeText(commentTime);
            commentController.setUploaderImage(userPhoto); // Set the user's image
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
            DatabaseConnection.establishConnection();
            String sql = "INSERT INTO comments (commentText, commentTime, CommentDate, postID, student_username, teacher_username) " +
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

        try {
            // Fetch and display comments for the current post
            String query = "SELECT commentID, commentText, commentDate, commentTime, student_username, teacher_username FROM comments WHERE postID = ?";
            Connection connection = DatabaseConnection.getConnection();
            DatabaseConnection.establishConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, currentPost.currentPostId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String commentText = resultSet.getString("commentText");
                String commentDate = resultSet.getString("commentDate");
                String commentTime = resultSet.getString("commentTime");
                String userName = "";
                String studentUsername = resultSet.getString("student_username");
                String teacherUsername = resultSet.getString("teacher_username");
                if (studentUsername != null) {
                    userName = studentUsername;
                } else if (teacherUsername != null) {
                    userName = teacherUsername;
                }
                Blob userPhotoBlob = getUserPhotoBlob(resultSet);

                addComment(commentText, commentDate, commentTime,userName , userPhotoBlob);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Blob getUserPhotoBlob(ResultSet resultSet) throws SQLException {
        String studentUsername = resultSet.getString("student_username");
        String teacherUsername = resultSet.getString("teacher_username");

        // Determine whether the comment is made by a student or teacher
        if (studentUsername != null) {
            // Retrieve the user's photo Blob from the students table
            String photoQuery = "SELECT photo FROM students WHERE student_username = ?";
            return getUserPhotoBlobFromTable(photoQuery, studentUsername);
        } else if (teacherUsername != null) {
            // Retrieve the user's photo Blob from the teachers table
            String photoQuery = "SELECT photo FROM teachers WHERE teacher_username = ?";
            return getUserPhotoBlobFromTable(photoQuery, teacherUsername);
        }

        return null; // Return null if no user photo found
    }

    private Blob getUserPhotoBlobFromTable(String query, String username) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet photoResultSet = preparedStatement.executeQuery();

        if (photoResultSet.next()) {
            return photoResultSet.getBlob("photo");
        }
        return null; // Return null if no user photo found
    }


}
