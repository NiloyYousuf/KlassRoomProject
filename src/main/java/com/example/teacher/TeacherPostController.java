package com.example.teacher;
import com.example.DAO.AttachmentDAO;

import com.example.klassroom.DatabaseConnection;
import com.example.klassroom.GlobalFxmlString;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
    private TextField AttachmentLabel;

    @FXML
    private VBox commentVBox;

    @FXML
    private Button deleteButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Date.setText(TeacherPostTileController.current_post.getPostDate());
        Time.setText(TeacherPostTileController.current_post.getPostTime());
        Post.setText(TeacherPostTileController.current_post.getPostText());
       // System.out.println(TeacherPostTileController.current_post.getPostText() + " This is the text of the comment");
        //System.out.println("Debug current post ID  " + currentPost.currentPostId + TeacherPostTileController.current_post.getPostId());
        // Fetch and print comments for the current post
      //  printCommentsForPost(TeacherPostTileController.current_post.getPostId());
        loadCommentSection();


        int postId = TeacherPostTileController.current_post.getPostId();
        if (AttachmentDAO.getAttachment(postId) != null && AttachmentDAO.getOriginalFilename(postId) != null) {
            AttachmentLabel.setText("Attachment Available");
        } else {
            AttachmentLabel.setText("No Attachment");
            Download.setDisable(true); // Disable the download button if no attachment
        }
    }


    @FXML
    private void handleDownloadButton() {
        int postId = TeacherPostTileController.current_post.getPostId();
        Window ownerWindow = Download.getScene().getWindow(); // Use the window of the button as the owner
        AttachmentDAO.downloadAttachment(postId, ownerWindow);
    }


    public void loadCommentSection() {
        try {
            // Load the CommentSection.fxml
            FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Comment/commentSection.fxml").toURL());
            AnchorPane commentSection = loader.load();

            // Add the CommentSection to the commentVBox
            commentVBox.getChildren().add(commentSection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





        public void Go_back()
    {
      loadAnotherFXML();

    }

    @FXML
    private void handleDeleteButton() {
        int postId = TeacherPostTileController.current_post.getPostId();


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Post");
        alert.setContentText("Are you sure you want to delete this post?");

        // Customize the buttons in the alert
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        // Show the confirmation alert and wait for user response
        ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
        if (result == ButtonType.YES) {
            // TODO: Add code to delete the post from the posts table
            deletePost(postId);

            // Load another FXML file after successful deletion
            loadAnotherFXML();
        }

    }

    private void deletePost(int postId) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            DatabaseConnection.establishConnection();

            // SQL query to delete the post
            String deleteSql = "DELETE FROM posts WHERE post_id = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
            deleteStatement.setInt(1, postId);

            // Execute the delete statement
            int rowsDeleted = deleteStatement.executeUpdate();

            if (rowsDeleted > 0) {
                //System.out.println("Post deleted successfully.");
            } else {
                //System.out.println("Failed to delete the post.");
            }

            // Close the resources

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAnotherFXML() {
        try {
            // Load the new FXML file
            GlobalFxmlString.FXML_to_load = "src/main/resources/com/example/klassroom/classroomTeacher.fxml";
            FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Dashboards/TeacherFinalDashboard.fxml").toURL());
            // Get the current stage (assuming you have a reference to the current stage)

            Parent post = loader.load();
            // Get the current scene and set the student login content
            Scene currentScene =Download.getScene();
            currentScene.setRoot(post);




        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void printCommentsForPost(int postId) {
        try {
            // Replace with your database connection details
            Connection connection = DatabaseConnection.getConnection();
            // SQL query to fetch comments for the given postID
            String sql = "SELECT commentID, commentText, commentTime, CommentDate, student_username, teacher_username FROM comments WHERE postID = ?";
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


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

