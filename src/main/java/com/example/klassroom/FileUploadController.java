package com.example.klassroom;
import com.example.Database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class FileUploadController {
    @FXML
    private Label selectedFileLabel;

    private File selectedFile;
    private String originalFilename; // Add the original filename field

    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            selectedFile = file;
            originalFilename = file.getName(); // Store the original filename
            selectedFileLabel.setText("Selected File: " + originalFilename);
        }
    }

    public void uploadFile() {
        if (selectedFile != null) {
            try {
                Connection connection = DatabaseConnection.getConnection();
                String sql = "INSERT INTO posts (classroom_id, post_time, post_date, post_text, attachment, original_filename) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                // Set the parameters
                preparedStatement.setInt(1, 1); // Replace with the actual classroom ID
                preparedStatement.setTime(2, Time.valueOf(LocalTime.now())); // Current time
                preparedStatement.setDate(3, Date.valueOf(LocalDate.now())); // Current date
                preparedStatement.setString(4, "lolhahaha"); // Replace with the actual post text

                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                preparedStatement.setBinaryStream(5, (InputStream) fileInputStream, (int) selectedFile.length());

                // Set the original filename
                preparedStatement.setString(6, originalFilename);

                preparedStatement.executeUpdate();

                fileInputStream.close();
                preparedStatement.close();
                connection.close();

                selectedFileLabel.setText("File uploaded successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                selectedFileLabel.setText("Error uploading file.");
            }
        } else {
            selectedFileLabel.setText("No file selected.");
        }
    }
}
