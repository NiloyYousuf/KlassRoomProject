package com.example.klassroom;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;

public class TeacherPostTileController {

    public static Post current_post;



    @FXML
    private Label postTimeLabel;

    @FXML
    private Label postDateLabel;

    @FXML
    private Label postTextLabel;


    @FXML
    private  Label Post_Id;

    public void setPostData(Post post) {
        postTimeLabel.setText(post.getPostTime());
        postDateLabel.setText(post.getPostDate());
        postTextLabel.setText(post.getPostText());
        Post_Id.setText(post.getPostId().toString());
        // Set other elements if needed
    }

    public  void see_full_post_clicked() throws IOException {
        current_post=ClassroomPostsDAO.getPostById(Integer.parseInt(Post_Id.getText()));
        currentPost.currentPostId=Integer.parseInt(Post_Id.getText());
        //System.out.println(Post_Id.getText());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TeacherPost.fxml"));
        Parent post = loader.load();
        // Get the current scene and set the student login content
        Scene currentScene =postTimeLabel.getScene();
        currentScene.setRoot(post);
    }

}
