package com.example.klassroom;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StudentPostController implements Initializable {

    @FXML
    private Label Date;

    @FXML
    private Label selectedPostIdLabel;

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
        Date.setText(StudentPostTileController.current_post.getPostDate());
        Time.setText(StudentPostTileController.current_post.getPostTime());
        Post.setText(StudentPostTileController.current_post.getPostText());
       // System.out.println(StudentPostTileController.current_post.getPostText()+ " this was written");
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
            GlobalFxmlString.FXML_to_load="ClassroomStudent.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentFinalDashboard.fxml"));
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



    public void downloadFile() {
        int selectedPostId = currentPost.currentPostId;
        if (selectedPostId > 0) {
            try {
                Connection connection = DatabaseConnection.getConnection();
                String sql = "SELECT attachment, original_filename FROM posts WHERE post_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, selectedPostId);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    InputStream inputStream = resultSet.getBinaryStream("attachment");
                    String originalFilename = resultSet.getString("original_filename");

                    if (inputStream != null && originalFilename != null) {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Save File");
                        fileChooser.setInitialFileName(originalFilename); // Set the suggested file name

                        File file = fileChooser.showSaveDialog(null);

                        if (file != null) {
                            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                                byte[] buffer = new byte[1024];
                                int bytesRead;
                                while ((bytesRead = inputStream.read(buffer)) != -1) {
                                    fileOutputStream.write(buffer, 0, bytesRead);
                                }
                            }

                            selectedPostIdLabel.setText("File downloaded successfully.");
                        } else {
                            selectedPostIdLabel.setText("Download canceled.");
                        }
                    } else {
                        showAlert("File not found", "The specified post ID does not contain an attachment.");
                    }
                } else {
                    showAlert("File not found", "The specified post ID does not exist.");
                }

                preparedStatement.close();
                connection.close();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred while downloading the file.");
            }
        } else {
            showAlert("No Post ID", "Please select a post ID to download the file.");
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}

