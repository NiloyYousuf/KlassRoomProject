package com.example.klassroom;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainCSS extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a VBox
        VBox root = new VBox();

        // Set up the scene
        Scene scene = new Scene(root, 300, 200);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        // Set up two text fields
        javafx.scene.control.TextField textField1 = new javafx.scene.control.TextField();
        textField1.setPromptText("Enter your name");
        textField1.getStyleClass().add("custom-text-field");

        javafx.scene.control.TextField textField2 = new javafx.scene.control.TextField();
        textField2.setPromptText("Enter your email");
        textField2.getStyleClass().add("custom-text-field");

        // Add the text fields to the root
        root.getChildren().addAll(textField1, textField2);

        // Show the stage
        primaryStage.setTitle("Styled Text Fields");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

