package com.example.klassroom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AssignmentPostApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AssignmentPostForm.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Post Assignment");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}

