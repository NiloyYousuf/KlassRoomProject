package com.example.klassroom;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentPostController implements Initializable {

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
        Date.setText(StudentPostTileController.current_post.getPostDate());
        Time.setText(StudentPostTileController.current_post.getPostTime());
        Post.setText(StudentPostTileController.current_post.getPostText());
        System.out.println(StudentPostTileController.current_post.getPostText()+ " this was written");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ClassroomStudent.fxml"));
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

}

