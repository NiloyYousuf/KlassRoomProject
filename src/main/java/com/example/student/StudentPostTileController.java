package com.example.student;

import com.example.klassroom.ClassroomPostsDAO;
import com.example.klassroom.GlobalFxmlString;
import com.example.klassroom.Post;
import com.example.Current_Variables.currentPost;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;

public class StudentPostTileController {

    public static Post current_post;



    @FXML
    private Label postTimeLabel;

    @FXML
    private Label postDateLabel;

    @FXML
    private Label postTextLabel;

    @FXML
    private TextField Postfield;

    @FXML
    private  Label Post_Id;

    public void setPostData(Post post) {
        postTimeLabel.setText(post.getPostTime());
        postDateLabel.setText(post.getPostDate());
        Postfield.setText(post.getPostText());
        Post_Id.setText(post.getPostId().toString());
        // Set other elements if needed
    }

    public  void see_full_post_clicked() throws IOException {
        current_post= ClassroomPostsDAO.getPostById(Integer.parseInt(Post_Id.getText()));
        currentPost.currentPostId=Integer.parseInt(Post_Id.getText());
       // System.out.println(Post_Id.getText());
        GlobalFxmlString.FXML_to_load="src/main/resources/com/example/Post/StudentPost.fxml";
        FXMLLoader loader = new FXMLLoader(new File("src/main/resources/com/example/Dashboards/StudentFinalDashboard.fxml").toURL());
        Parent post = loader.load();
        // Get the current scene and set the student login content
        Scene currentScene =postTimeLabel.getScene();
        currentScene.setRoot(post);
    }

}
