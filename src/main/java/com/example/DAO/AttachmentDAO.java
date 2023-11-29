package com.example.DAO;

import com.example.klassroom.DatabaseConnection;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AttachmentDAO {

    public static byte[] getAttachment(int postId) {
        byte[] attachment = null;

        try (Connection connection = DatabaseConnection.getConnection()) {
            DatabaseConnection.establishConnection();
            String sql = "SELECT attachment FROM posts WHERE post_id = ?";
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             preparedStatement.setInt(1, postId);
              ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        attachment = resultSet.getBytes("attachment");
                    }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attachment;
    }

    public static String getOriginalFilename(int postId) {
        String originalFilename = null;

        try (Connection connection = DatabaseConnection.getConnection()) {
            DatabaseConnection.establishConnection();
            String sql = "SELECT original_filename FROM posts WHERE post_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, postId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        originalFilename = resultSet.getString("original_filename");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return originalFilename;
    }

    public static void downloadAttachment(int postId, Window ownerWindow) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT attachment, original_filename FROM posts WHERE post_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, postId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        byte[] attachment = resultSet.getBytes("attachment");
                        String originalFilename = resultSet.getString("original_filename");

                        if (attachment != null && originalFilename != null) {
                            FileChooser fileChooser = new FileChooser();
                            fileChooser.setTitle("Save Attachment");
                            fileChooser.setInitialFileName(originalFilename);

                            File selectedFile = fileChooser.showSaveDialog(ownerWindow);

                            if (selectedFile != null) {
                                try (InputStream inputStream = new ByteArrayInputStream(attachment)) {
                                    Files.copy(inputStream, selectedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                    System.out.println("Attachment downloaded successfully.");
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
