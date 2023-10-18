package com.example.klassroom;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class classroomStudentController implements Initializable {

    @FXML
    private TextField studentNameTextField;

    @FXML
    private TextField courseCodeTextField;

    @FXML
    private TextField subjectNameTextField;

    @FXML
    private ScrollPane scrollPane; // ScrollPane to make content scrollable

    @FXML
    private VBox postContainer; // VBox to hold post tiles

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            studentNameTextField.setText(StudentMenuController.student_username);
            courseCodeTextField.setText(CurrentClassroom.getClassroomCode());
            subjectNameTextField.setText(CurrentClassroom.getSubjectName());
            System.out.println(CurrentClassroom.getClassroomId());

            ClassroomPostsDAO.printAllPosts(CurrentClassroom.getClassroomId());
            List<Post> posts = ClassroomPostsDAO.getPostsByClassroom(CurrentClassroom.getClassroomId());

            // Clear existing items in the postContainer
            postContainer.getChildren().clear();
           // scheduler.scheduleAtFixedRate(this::refreshPostContainer, 0, 5, TimeUnit.SECONDS);
            // Populate the VBox with post tiles
            for (Post post : posts) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Student_post_tile.fxml"));
                AnchorPane postTile;
                try {
                    postTile = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // Set the post data in the Student_post_tile.fxml controller
                StudentPostTileController postTileController = loader.getController();
                postTileController.setPostData(post); // Implement this method in your StudentPostTileController

                // Add the post tile to the VBox
                postContainer.getChildren().add(postTile);
            }
        });
    }

    // Method to refresh the postContainer with the latest posts
    private void refreshPostContainer() {
        List<Post> posts = ClassroomPostsDAO.getPostsByClassroom(CurrentClassroom.getClassroomId());

        // Clear existing items in the postContainer
        postContainer.getChildren().clear();

        // Populate the VBox with post tiles
        for (Post post : posts) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Student_post_tile.fxml"));
            AnchorPane postTile;
            try {
                postTile = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Set the post data in the Student_post_tile.fxml controller
            StudentPostTileController postTileController = loader.getController();
            postTileController.setPostData(post); // Implement this method in your StudentPostTileController

            // Add the post tile to the VBox
            postContainer.getChildren().add(postTile);
        }
        System.out.println("Posts refreshed");
    }

    // Don't forget to stop the scheduler when application exits
    public static void stopScheduler() {
        scheduler.shutdown();
    }


    public void Go_back()
    {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentFinalDashboard.fxml"));
            Parent root = loader.load();

            // Get the current stage (assuming you have a reference to the current stage)
            Stage stage = (Stage)studentNameTextField.getScene().getWindow();

            // Set the new FXML content on the current stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
