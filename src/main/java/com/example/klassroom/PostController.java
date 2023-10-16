package com.example.klassroom;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class PostController implements Initializable {

    @FXML
    private Label Date;

    @FXML
    private Label Time;

    @FXML
    private TextArea Post;

    @FXML
    private Button Download;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Date.setText(PostTileController.current_post.getPostDate());
        Time.setText(PostTileController.current_post.getPostTime());
        Post.setText(PostTileController.current_post.getPostText());
        System.out.println(PostTileController.current_post.getPostText()+ " this was written");
    }

}

