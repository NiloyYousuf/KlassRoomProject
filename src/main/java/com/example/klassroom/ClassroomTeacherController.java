package com.example.klassroom;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClassroomTeacherController implements Initializable {

    @FXML
    private TextField teacherNameTextField;

    @FXML
    private TextField courseCodeTextField;

    @FXML
    private TextField subjectNameTextField;

    @FXML
    private ScrollPane scrollPane; // ScrollPane to make content scrollable

    @FXML
    private VBox postContainer; // VBox to hold post tiles

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            teacherNameTextField.setText(CurrentClassroom.getTeacherUsername());
            courseCodeTextField.setText(CurrentClassroom.getClassroomCode());
            subjectNameTextField.setText(CurrentClassroom.getSubjectName());
            System.out.println(CurrentClassroom.getClassroomId());

            ClassroomPostsDAO.printAllPosts(CurrentClassroom.getClassroomId());
            List<Post> posts = ClassroomPostsDAO.getPostsByClassroom(CurrentClassroom.getClassroomId());

            // Clear existing items in the postContainer
            postContainer.getChildren().clear();

            // Populate the VBox with post tiles
            for (Post post : posts) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("post_tile.fxml"));
                AnchorPane postTile;
                try {
                    postTile = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // Set the post data in the post_tile.fxml controller
                PostTileController postTileController = loader.getController();
                postTileController.setPostData(post); // Implement this method in your PostTileController

                // Add the post tile to the VBox
                postContainer.getChildren().add(postTile);
            }
        });
    }


    @FXML
    private Button Make_post_button;


    @FXML
    private TextArea post;
    @FXML
    private void make_post_button_clicked()
    {
        long millis=System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        System.out.println(date);
        String location = new String();
        ClassroomPostsDAO.insertPost(CurrentClassroom.getClassroomId(),java.time.LocalTime.now().toString(),date.toString(),post.getText(), location.getBytes());
        refreshPostContainer();
    }

    private void refreshPostContainer() {
        List<Post> posts = ClassroomPostsDAO.getPostsByClassroom(CurrentClassroom.getClassroomId());

        // Clear existing items in the postContainer
        postContainer.getChildren().clear();

        // Populate the VBox with post tiles
        for (Post post : posts) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("post_tile.fxml"));
            AnchorPane postTile;
            try {
                postTile = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Set the post data in the post_tile.fxml controller
            PostTileController postTileController = loader.getController();
            postTileController.setPostData(post); // Implement this method in your PostTileController

            // Add the post tile to the VBox
            postContainer.getChildren().add(postTile);
        }
    }

@FXML
private  Button attendance;

    @FXML
    private  void  attendance_button_clicked() throws IOException {
        StudentListController.classroomCode=CurrentClassroom.getClassroomCode();
        System.out.println(StudentListController.classroomCode);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("student_list.fxml"));
        Parent studentLogin = loader.load();
        // Get the current scene and set the student login content
        Scene currentScene = attendance.getScene();
        currentScene.setRoot(studentLogin);
    }
}
