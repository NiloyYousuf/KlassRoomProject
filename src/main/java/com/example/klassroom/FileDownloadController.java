package com.example.klassroom;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileDownloadController {
    @FXML
    private Label selectedPostIdLabel;

    private int selectedPostId=37; // The post_id to download

    // ...

    public void downloadFile() {
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
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
